package cf.example.mod;

import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.jobs.JobManager;

public class SampleModule extends Module {

	public SampleModule(Mod mod) {
		super("SampleModule", mod);
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new SampleJob(this));
	}

}
