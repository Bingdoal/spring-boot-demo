package springboot.test.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import springboot.test.utils.DatasourceInfo;

@Component
@Slf4j
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    DatasourceInfo datasourceInfo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        flywayMigrate();
    }

    private void flywayMigrate() {
        final Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .target(MigrationVersion.fromVersion(datasourceInfo.getMigrationVersion()))
                .dataSource(datasourceInfo.getUrl(), datasourceInfo.getUsername(), datasourceInfo.getPassword())
                .schemas(datasourceInfo.getSchema()).load();
        final MigrateResult migrateResult = flyway.migrate();
        flyway.validate();
        final MigrationInfo info = flyway.info().current();
        log.info("\tFlyway Status: TargetVersion={}, state={}, file name=V{}__{}", migrateResult.targetSchemaVersion, info.getState(), info.getVersion(),
                info.getDescription());
    }
}
