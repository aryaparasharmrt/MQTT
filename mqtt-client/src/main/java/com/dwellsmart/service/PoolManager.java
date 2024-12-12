package com.dwellsmart.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.dto.ResponseError;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.util.PayloadUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PoolManager {

	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private PayloadUtils payloadUtils;
	
	@Autowired
	private MeterOperationService meterOperationService;

	private volatile int corePoolSize = 5;
	private volatile int maximumPoolSize = 8;
	private volatile long keepAliveTime = 30L; // In minutes
	private volatile RejectedExecutionHandler handler = new RejectedTask();
	private volatile ThreadFactory threadFactory = Executors.defaultThreadFactory();
	private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

	private final ExecutorService threadPool;

	public PoolManager() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				TimeUnit.MINUTES, workQueue, threadFactory, handler);
		executor.allowCoreThreadTimeOut(true);
		threadPool = executor;
	}

	public void processPayload(String payload, MQTTService mqttService) {

		threadPool.submit(() -> {
			log.debug("Processing payload: " + payload);
			byte[] response = null;
			try {
				MeterOperationPayload operationPayload = payloadUtils.validateRequest(payload);

				// Validate message ID
				if (!cacheService.isValid(operationPayload.getMessageId())) {
					throw new ApplicationException("Duplicate message id request rejected within 10 minutes, message id: " + operationPayload.getMessageId());
				}

				// Main Functionality:
				meterOperationService.processOperation(operationPayload);

				log.debug("Returning payload: \n" + operationPayload);

				response = payloadUtils.convertToResponseAsBytes(operationPayload);

			} catch (ApplicationException e) {
				log.warn("Application Exception: " + e.getLocalizedMessage());
//				e.printStackTrace(); // TODO for logging errorsS
				ResponseError responseError = ResponseError.builder().errorCode(e.getCode())
						.errorMessage(e.getMessage()).build();

				log.debug("Response Error: \n" + responseError);
				response = payloadUtils.convertToResponseAsBytes(responseError);

			} catch (Exception e) {
				e.printStackTrace();
				ResponseError responseError = ResponseError.builder()
						.errorCode(ErrorCode.GENERIC_EXCEPTION.getErrorCode())
						.errorMessage(ErrorCode.GENERIC_EXCEPTION.getErrorMessage()).build();
				log.debug("Response Error: \n" + responseError);
				response = payloadUtils.convertToResponseAsBytes(responseError);
			}
			mqttService.defaultPublish(response);

			log.info("Completed payload processing: " +payload);
		});
	}

	static class RejectedTask implements RejectedExecutionHandler {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			log.error("Task rejected: " + r.toString());
		}

	}

}
