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

package org.springframework.cloud.aws.context.config.annotation;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.aws.context.support.io.ResourceLoaderBeanPostProcessor;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientConfigurationUtils;
import org.springframework.cloud.aws.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.cloud.aws.core.io.s3.SimpleStorageResourceLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Agim Emruli
 */
@Configuration
public class ContextResourceLoaderConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		BeanDefinitionHolder client = AmazonWebserviceClientConfigurationUtils.registerAmazonWebserviceClient(this, registry, AmazonS3Client.class.getName(), null, null);

		//TODO: Add support for specifying a custom task executor
		BeanDefinitionBuilder resolver = BeanDefinitionBuilder.genericBeanDefinition(SimpleStorageResourceLoader.class);
		resolver.addConstructorArgReference(client.getBeanName());

		BeanDefinitionBuilder pathMatcher = BeanDefinitionBuilder.genericBeanDefinition(PathMatchingSimpleStorageResourcePatternResolver.class);
		pathMatcher.addConstructorArgReference(client.getBeanName());
		pathMatcher.addConstructorArgValue(resolver.getBeanDefinition());

		String resolverBeanName = BeanDefinitionReaderUtils.registerWithGeneratedName(pathMatcher.getBeanDefinition(), registry);

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ResourceLoaderBeanPostProcessor.class);
		beanDefinitionBuilder.addConstructorArgReference(resolverBeanName);

		BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
	}
}
