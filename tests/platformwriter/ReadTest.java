package platformwriter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import com.airbus.vibe.dalo.PlatformWriter;

public class ReadTest {
	
	PlatformWriter pw; 
	
	@Before
    public void setUp() {
		System.out.println("==== creating a new PlatformWriter from file ====");
		String input_s = "inputs/platformwriter/Platforms_test.xml";
		pw = new PlatformWriter(input_s,"OLIVIER");        
    }
	
	@Test
	public void testRead() {
		System.out.println("===== test Read =====");
		System.out.println(pw.tree);
		assertNotNull(pw.getDocument());
	}

	@Test
	public void testWrite() {
		System.out.println("===== test Write =====");
		
		File output_f = new File("outputs/platformwriter/reduced.xml");
		
		try {
			output_f.createNewFile();
		} catch (IOException e) {
			System.err.println("check permissions!");
			System.err.println("make sure output dir exists");
			e.printStackTrace();
			fail("io error");
		}

		//System.out.println(pw.c_tree);

		System.out.println(" --- disabling some of the " + pw.getFinalList().size() + " elements---");
		
		pw.getFinalList().get(0).setIncluded(false);
		pw.getFinalList().get(3).setIncluded(false);
		pw.getFinalList().get( pw.getFinalList().size()-1 ).setIncluded(false);
		
		System.out.println("writing first tree:");
		System.out.println(pw.tree);
		pw.write(output_f);
		
		//###################################
		
		System.out.println("--- re-enabling last element ---");
		pw.getFinalList().get( pw.getFinalList().size()-1 ).setIncluded(true);
		
		output_f = new File("outputs/platformwriter/reduced_2.xml");
		
		try {
			output_f.createNewFile();
		} catch (IOException e) {
			System.err.println("check permissions!");
			System.err.println("make sure output dir exists");
			e.printStackTrace();
			fail("io error");
		}
		
		System.out.println("writing second tree:");
		System.out.println(pw.tree);
		pw.write(output_f);

		assertNotNull(pw.getDocument());	
	}

}
