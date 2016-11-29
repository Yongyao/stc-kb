package esiptestbed.stc_kb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import edu.knowitall.openie.Argument;
import edu.knowitall.openie.Instance;
import edu.knowitall.openie.OpenIE;
import edu.knowitall.tool.parse.ClearParser;
import edu.knowitall.tool.postag.ClearPostagger;
import edu.knowitall.tool.srl.ClearSrl;
import edu.knowitall.tool.tokenize.ClearTokenizer;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * Read the a txt file and print out the results in the console
 * @author Yongyao
 *
 */
public class TestOpenIE {

	public TestOpenIE() {
		// TODO Auto-generated constructor stub
	}

	static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		SentenceDetector sentenceDetector = null;
		InputStream modelIn = null;

		try {
			File fi = new File("C:/Users/Yongyao/Desktop/papers/reverb/en-sent.bin");
			modelIn = new FileInputStream(fi);
			final SentenceModel sentenceModel = new SentenceModel(modelIn);
			modelIn.close();
			sentenceDetector = new SentenceDetectorME(sentenceModel);
		}
		catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("---Started---");
		OpenIE openIE = new OpenIE(new ClearParser(new ClearPostagger(new ClearTokenizer())), new ClearSrl(), false, false);

		File file = new File("C:/Users/Yongyao/Desktop/papers/reverb/test.txt");
		String text = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);

		String sentences[]= sentenceDetector.sentDetect(text);
		for(int i=0; i<sentences.length; i++)
		{

			Seq<Instance> extractions = openIE.extract(sentences[i]);

			List<Instance> list_extractions = JavaConversions.seqAsJavaList(extractions);


			for(Instance instance : list_extractions) {
				StringBuilder sb = new StringBuilder();

				sb.append(instance.confidence())
				.append('\t')
				.append(instance.extr().context())
				.append('\t')
				.append(instance.extr().arg1().text())
				.append('\t')
				.append(instance.extr().rel().text())
				.append('\t');

				List<Argument> list_arg2s = JavaConversions.seqAsJavaList(instance.extr().arg2s());
				for(Argument argument : list_arg2s) {
					sb.append(argument.text()).append("; ");
				}

				System.out.println(sb.toString());
			}
		}

	}

}
