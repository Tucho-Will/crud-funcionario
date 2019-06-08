package crud.core.gson;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import crud.util.i18n.Messages;

/**
 * Gerador de Gson ja configurado a serialização no formato correto de
 * ZonedDateTime
 * 
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public class GsonHelper {

	public final static JsonDeserializer<ZonedDateTime> ZDT_DESERIALIZER = new JsonDeserializer<ZonedDateTime>() {
		@Override
		public ZonedDateTime deserialize(JsonElement json, Type type,
				JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			DateTimeFormatter format = DateTimeFormatter
					.ofPattern(Messages.getString("GsonHelper.zoneDateTime.formatter")) //$NON-NLS-1$
					.withZone(ZoneId.of(Messages.getString("GsonHelper.zoneId"))); //$NON-NLS-1$
			try {
				return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
			} catch (RuntimeException e) {
				throw new JsonParseException(Messages.getString("GsonHelper.exception.message"), e); //$NON-NLS-1$
			}
		}
	};

	public final static JsonSerializer<ZonedDateTime> ZDT_SERIALIZER = new JsonSerializer<ZonedDateTime>() {
		@Override
		public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
			DateTimeFormatter format = DateTimeFormatter
					.ofPattern(Messages.getString("GsonHelper.zoneDateTime.formatter")) //$NON-NLS-1$
					.withZone(ZoneId.of(Messages.getString("GsonHelper.zoneId"))); //$NON-NLS-1$
			return new JsonPrimitive(src.format(format));
		}
	};

	public static Gson gson = new GsonBuilder()//
			.registerTypeAdapter(ZonedDateTime.class, GsonHelper.ZDT_SERIALIZER)//
			.registerTypeAdapter(ZonedDateTime.class, GsonHelper.ZDT_DESERIALIZER).enableComplexMapKeySerialization()//
			.setDateFormat(Messages.getString("GsonHelper.date.formatter"))// //$NON-NLS-1$
			.serializeNulls()//
			.create();

}
