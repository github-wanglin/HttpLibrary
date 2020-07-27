package com.wl.library.http.part;



import com.wl.library.http.request.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author:wanglin
 * date:2020/7/8 3:26 PM
 * description:NetworkDemo
 */
public class MultipartBody extends RequestBody {
    public static final String disposition = "content-disposition: form-data; ";
    public static final byte[] END_LINE = {'\r', '\n'};
    public static final byte[] PREFIX = {'-', '-'};

    final List<Part> parts;
    final String boundary;

    public MultipartBody(Builder builder) {
        this.parts = builder.parts;
        this.boundary = builder.boundary;
    }

    @Override
    public String contentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        try {
            for (Part part : parts) {
                outputStream.write(PREFIX);
                outputStream.write(boundary.getBytes(StandardCharsets.UTF_8));
                outputStream.write(END_LINE);
                part.write(outputStream);
            }
            outputStream.write(PREFIX);
            outputStream.write(boundary.getBytes(StandardCharsets.UTF_8));
            outputStream.write(PREFIX);
            outputStream.write(END_LINE);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static class Builder {
        private String boundary;
        private List<Part> parts;

        public Builder() {
            this(UUID.randomUUID().toString());
        }

        private Builder(String boundary) {
            this.parts = new ArrayList<>();
            this.boundary = boundary;
        }

        public Builder addPart(String type, String key, File file) {
            if (key == null) throw new NullPointerException("part name == null");
            parts.add(Part.create(type, key, file));
            return this;
        }

        public Builder addForm(String key, String value) {
            if (key == null) throw new NullPointerException("part name == null");
            parts.add(Part.create(key, value));
            return this;
        }

        public MultipartBody build() {
            if (parts.isEmpty()) throw new NullPointerException("part list == null");
            return new MultipartBody(this);
        }
    }
}
