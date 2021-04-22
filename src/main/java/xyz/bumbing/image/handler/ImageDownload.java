package xyz.bumbing.image.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

public class ImageDownload {

    public static void download(String url) {
	URI uri = URI.create(url);

	String path2 = uri.getPath();

	Flux<DataBuffer> dataBufferFlux = WebClient.create().get().uri(URI.create(url)).retrieve()
		.bodyToFlux(DataBuffer.class); // the magic happens here

	File file = new File("download" + path2);
	file.getParentFile().mkdirs();
	final Path path = file.toPath();
	DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block();

    }

    public static void main(String[] args) {

	FileReader reader;
	try {
	    reader = new FileReader(new File("url.txt"));
	    BufferedReader br = new BufferedReader(reader);
	    String line = null;

	    while ((line = br.readLine()) != null) {
		download(line);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
}
