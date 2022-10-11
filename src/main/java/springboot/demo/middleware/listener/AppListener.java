package springboot.demo.middleware.listener;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.MigrateResult;
import org.flywaydb.core.api.output.UndoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import springboot.demo.service.TransactionalTestService;
import springboot.demo.utils.properties.DatasourceInfo;
import springboot.demo.utils.properties.FlywayInfo;

@Component
@Slf4j
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private FlywayInfo flywayInfo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        flywayMigrate();
    }

    private void flywayMigrate() {
        final Flyway flyway = Flyway.configure()
                .locations(flywayInfo.getLocations())
                .baselineVersion(flywayInfo.getBaselineVersion())
                .baselineOnMigrate(flywayInfo.isBaselineOnMigrate())
                .validateOnMigrate(flywayInfo.isValidateOnMigrate())
                .outOfOrder(flywayInfo.isOutOfOrder())
                .target(MigrationVersion.fromVersion(flywayInfo.getTarget()))
                .dataSource(flywayInfo.getUrl(), flywayInfo.getUser(), flywayInfo.getPassword())
                .defaultSchema(flywayInfo.getDefaultSchema()).load();

//        TODO: 自動判斷版本並進行 undo 與 migration，不過 undo 的功能需要付費使用，暫時不支援
//        if (flyway.info().current().getVersion().isNewerThan(target)) {
//            UndoResult undoResult = flyway.undo();
//            final MigrationInfo info = flyway.info().current();
//            log.info("\tFlyway Status: TargetVersion={}, state={}, file name=V{}__{}", undoResult.targetSchemaVersion, info.getState(), info.getVersion(),
//                    info.getDescription());
//        } else {
//            final MigrateResult migrateResult = flyway.migrate();
//            final MigrationInfo info = flyway.info().current();
//            log.info("\tFlyway Status: TargetVersion={}, state={}, file name=V{}__{}", migrateResult.targetSchemaVersion, info.getState(), info.getVersion(),
//                    info.getDescription());
//        }

        final MigrateResult migrateResult = flyway.migrate();
        final MigrationInfo info = flyway.info().current();
        log.info("\tFlyway Status: TargetVersion={}, state={}, file name=V{}__{}", migrateResult.targetSchemaVersion, info.getState(), info.getVersion(),
                info.getDescription());
    }
}
