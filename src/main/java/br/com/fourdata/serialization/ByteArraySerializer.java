package br.com.fourdata.serialization;

import java.lang.reflect.Type;

import org.apache.commons.codec.binary.Base64;

import br.com.caelum.vraptor.ioc.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class ByteArraySerializer implements JsonSerializer<byte[]> {

	@Override
	public JsonElement serialize(byte[] src, Type typeOfSrc,
			JsonSerializationContext context) {
		byte[] l2 = Base64.encodeBase64(src);
		return new JsonPrimitive(new String(l2));
	}

}