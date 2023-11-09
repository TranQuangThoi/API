package com.techmarket.api.repository;

import com.techmarket.api.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SettingsRepository extends JpaRepository<Settings, Long>, JpaSpecificationExecutor<Settings> {
    Settings findBySettingKey(String settingKey);
}
