1. Clone the repository
2. Run either:
   - Manual: Execute the manual install command (if needed)
   - Auto: Just run `mvn clean install` - the JAR will auto-install during build
   

Manual Command: run in the libs folder
$ mvnw.cmd install:install-file -Dfile=./DwellSMARTModBusTCPRTU.jar -DgroupId=com.dwellsmart -DartifactId=modbustcprtu -Dversion=1.0 -Dpackaging=jar