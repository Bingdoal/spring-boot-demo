package springboot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.demo.dto.CmdResultDto;
import springboot.demo.middleware.exception.RestException;
import springboot.demo.utils.properties.DatasourceInfo;
import springboot.demo.utils.properties.InfluxInfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class DbDumpService {
    @Autowired
    private CmdExecuteService cmdExecuteService;
    @Autowired
    private DatasourceInfo datasourceInfo;
    @Autowired
    private InfluxInfo influxInfo;

    private final Lock postgresLock = new ReentrantLock();
    private final Lock influxdbLock = new ReentrantLock();

    public void dumpPostgres(String outputFile) throws RestException {
        if (!postgresLock.tryLock()) {
            throw new RestException(400, "PostgreSQL dump is running...");
        }
        try {
            String setEnv;
            String cmd;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                setEnv = "set PGPASSWORD=" + datasourceInfo.getPassword() + "&& ";
                cmd = setEnv + " pg_dump -h " + datasourceInfo.getHost() + " -p " + datasourceInfo.getPort() +
                        " -U " + datasourceInfo.getUsername() + " -d " + datasourceInfo.getDatabase() +
                        " -f " + outputFile;
            } else {
                setEnv = "export PGPASSWORD=" + datasourceInfo.getPassword() + "&& ";
                cmd = setEnv + " /usr/bin/pg_dump -h " + datasourceInfo.getHost() + " -p " + datasourceInfo.getPort() +
                        " -U " + datasourceInfo.getUsername() + " -d " + datasourceInfo.getDatabase() +
                        " -f " + outputFile;
            }
            CmdResultDto resultDto = cmdExecuteService.execute(cmd);
            if (!resultDto.isSuccess()) {
                throw new RestException(500, "Dump postgres failed: "
                        + resultDto.getExitCode() + ": "
                        + resultDto.getError());
            }
        } catch (RestException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RestException(500, "Dump postgres failed: " + e.getMessage());
        } finally {
            postgresLock.unlock();
        }
    }

    public void dumpInfluxdb(String outputFile) throws RestException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            throw new RestException(500, "Influx dump cli not support on Windows");
        }
        if (!influxdbLock.tryLock()) {
            throw new RestException(400, "InfluxDB dump is running...");
        }
        try {
            String dumpDir = "influx_dump_" + System.currentTimeMillis();
            String cmd = "/usr/bin/influxd backup -portable -db " + influxInfo.getDatabase() +
                    " -host " + influxInfo.getHost() + ":" + influxInfo.getBackupPort() +
                    " " + dumpDir;
            cmd += " && tar -cf " + outputFile + " " + dumpDir;
            cmd += " && rm -rf " + dumpDir;
            CmdResultDto resultDto = cmdExecuteService.execute(cmd);
            if (!resultDto.isSuccess()) {
                throw new RestException(500, "Dump influxdb failed: "
                        + resultDto.getExitCode() + ": "
                        + resultDto.getError());
            }
        } catch (RestException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RestException(500, "Dump influxdb failed: " + e.getMessage());
        } finally {
            influxdbLock.unlock();
        }
    }
}
