/**
 * Created by  on 2017/8/14 0014.
 */
package rxfamily.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * 该类和gson中自带GsonRequestBodyConverter，但自带GsonRequestBodyConverter不是public的，外部无法引用，所以需要重写该类
 * @param <T>
 */
public class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> typeAdapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> typeAdaptert) {
        this.gson = gson;
        this.typeAdapter = typeAdaptert;
    }

    @Override public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        typeAdapter.write(jsonWriter,value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE,buffer.readByteString());
    }
}
