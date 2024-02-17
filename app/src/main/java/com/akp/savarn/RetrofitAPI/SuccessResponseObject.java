package com.akp.savarn.RetrofitAPI;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Anoop pandey-9696381023.
 */

public class SuccessResponseObject implements Converter {
    private Gson gson = new Gson();

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        try {
            InputStream in = typedInput.in(); // convert the typedInput to String
            String string = fromStream(in);
            in.close(); // we are responsible to close the InputStream after use
            // CommonUtill.print("api result ==" + string);
//            SuccessRequestResponse response = gson.fromJson(string, SuccessRequestResponse.class);
//            Result response = gson.fromJson(string, Result.class);
            return string;
//            if(response.status) {
//                return gson.fromJson(response.data, type);
//            }else{
//                throw new ConversionException(response.message);
//            }
        } catch (Exception e) { // a lot may happen here, whatever happens
            throw new ConversionException(e); // wrap it into ConversionException so retrofit can process it
        }
    }

    private static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append("\r\n");
        }
        return out.toString();
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            String charset = "UTF-8";
            String json = gson.toJson(object);
            return new JsonTypedOutput(json.getBytes(charset));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;

        JsonTypedOutput(byte[] jsonBytes) {
            this.jsonBytes = jsonBytes;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return "application/json; charset=UTF-8";
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }

//    public static class SuccessRequestResponse {
//        public boolean status;
//        public String message;
//        //        public JsonElement data;
//        public String data;
//    }

}
