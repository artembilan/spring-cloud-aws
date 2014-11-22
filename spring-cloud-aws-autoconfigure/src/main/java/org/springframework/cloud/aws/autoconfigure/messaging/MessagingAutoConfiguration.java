/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.aws.autoconfigure.messaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alain Sahli
 */
@ConditionalOnClass(name = "org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer")
@Configuration
public class MessagingAutoConfiguration {

	@ConditionalOnMissingBean(name = "org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer")
	@EnableSqs
	@Configuration
	public static class SqsAutoConfiguration {

	}

	@ConditionalOnClass(name = "org.springframework.web.method.support.HandlerMethodArgumentResolver")
	@EnableSns
	@Configuration
	public static class SnsAutoConfiguration {

	}

}