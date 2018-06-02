package cf.example.mod;

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;

public class SampleJob extends ModuleJob {
	
	public static SampleJob inst = null;
	
	public SampleJob(Module module) {
		super("SampleJob", "A sample job", module);
		inst = this;
	}
	
	@Override
	protected void runImpl() {
		Log.info("Sample job executing...");
	}

}
