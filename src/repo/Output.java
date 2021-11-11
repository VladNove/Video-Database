package repo;

import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Output {
	private static Writer fileWriter;
	private static JSONArray arrayResult;
	public static void setOutput(Writer writer, JSONArray jsonArray) {
		fileWriter = writer;
		arrayResult = jsonArray;
	}

	@SuppressWarnings("unchecked")
	public static void write(int id, String field, String message ) throws IOException {
		arrayResult.add(fileWriter.writeFile(id, field, message));
	}
}
