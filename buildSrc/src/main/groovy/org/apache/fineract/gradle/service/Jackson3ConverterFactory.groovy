/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.gradle.service

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import tools.jackson.databind.ObjectMapper

import java.lang.annotation.Annotation
import java.lang.reflect.Type

class Jackson3ConverterFactory extends Converter.Factory {

    private static final MediaType JSON = MediaType.get("application/json; charset=UTF-8")
    private final ObjectMapper mapper

    private Jackson3ConverterFactory(ObjectMapper mapper) {
        this.mapper = mapper
    }

    static Jackson3ConverterFactory create(ObjectMapper mapper) {
        return new Jackson3ConverterFactory(mapper)
    }

    @Override
    Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == Void.class) return null
        def javaType = mapper.typeFactory().constructType(type)
        return { ResponseBody body ->
            if (body == null) return null
            mapper.readValue(body.byteStream(), javaType)
        }
    }

    @Override
    Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return { Object value ->
            RequestBody.create(mapper.writeValueAsBytes(value), JSON)
        }
    }
}