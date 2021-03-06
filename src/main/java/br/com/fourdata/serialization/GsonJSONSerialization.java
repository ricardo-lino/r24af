/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package br.com.fourdata.serialization;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.serialization.NoRootSerialization;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;
import br.com.caelum.vraptor.view.ResultException;

import com.google.gson.JsonSerializer;

/**
 * XStream implementation for JSONSerialization
 * 
 * @author Lucas Cavalcanti
 * @since 3.0.2
 */
@Component
public class GsonJSONSerialization implements JSONSerialization {

	protected final HttpServletResponse response;

	protected final TypeNameExtractor extractor;

	protected final ProxyInitializer initializer;

	protected Collection<JsonSerializer<?>> adapters;

	protected final VraptorGsonBuilder builder;

	public GsonJSONSerialization(HttpServletResponse response, TypeNameExtractor extractor,
			ProxyInitializer initializer, Collection<JsonSerializer<?>> adapters) {
		this.response = response;
		this.extractor = extractor;
		this.initializer = initializer;
		this.adapters = adapters;

		this.builder = new VraptorGsonBuilder(adapters);
	}

	@Override
	public boolean accepts(String format) {
		return "json".equals(format);
	}

	@Override
	public <T> Serializer from(T object) {
		return from(object, null);
	}

	@Override
	public <T> Serializer from(T object, String alias) {
		response.setContentType("application/json");
		return getSerializer().from(object, alias);
	}

	protected SerializerBuilder getSerializer() {
		try {
			return new GsonSerializer(builder, response.getWriter(), extractor, initializer);
		} catch (IOException e) {
			throw new ResultException("Unable to serialize data", e);
		}
	}

	/**
	 * You can override this method for configuring Driver before serialization
	 */
	@Override
	public <T> NoRootSerialization withoutRoot() {
		builder.setWithoutRoot(true);
		return this;
	}

	@Override
	public JSONSerialization indented() {
		builder.indented();
		return this;
	}
}