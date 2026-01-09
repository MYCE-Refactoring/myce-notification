package com.myce.notification.repository;

import com.myce.notification.entity.MessageTemplateSetting;
import com.myce.notification.entity.type.ChannelType;
import com.myce.notification.entity.type.MessageTemplateCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTemplateSettingRepository extends JpaRepository<MessageTemplateSetting, Long> {

    Page<MessageTemplateSetting> findAll(Pageable pageable);

    Page<MessageTemplateSetting> findAllByNameContains(String name, Pageable pageable);

    Optional<MessageTemplateSetting> findByCodeAndChannelType(MessageTemplateCode code,  ChannelType channelType);

}
