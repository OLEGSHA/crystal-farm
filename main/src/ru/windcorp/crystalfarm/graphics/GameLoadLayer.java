/**
 * Crystal Farm the game
 * Copyright (C) 2018  Crystal Farm Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ru.windcorp.crystalfarm.graphics;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.crystalfarm.CrystalFarmLauncher;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontManager;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.crystalfarm.util.Direction;
import ru.windcorp.tge2.util.jobs.JobListener;
import ru.windcorp.tge2.util.jobs.JobManager;

public class GameLoadLayer extends Layer implements InputListener, JobListener<ModuleJob> {
	
	private static final int BAR_MARGIN_X = 100;
	private static final int BAR_MARGIN_Y = 20;
	private static final int BAR_HEIGHT = 20;
	private static final int BAR_THICKNESS = 5;
	
	private static final int TABLE_MARGIN_X = 50;
	
	private static final Color MAIN_COLOR = new Color(0xBF4C00FF);
	
	private final SimpleTexture logo = SimpleTexture.get("load/logo");
	private final Font font = FontManager.getDefaultFont();
	
	private final Map<Thread, ModuleJob> current = Collections.synchronizedMap(new HashMap<>());

	public GameLoadLayer() {
		super("GameLoad");
		CrystalFarmLauncher.getJobManager().addJobListener(this);
	}

	@Override
	public void render() {
		
		if (!CrystalFarmLauncher.doesJobManagerExist()) {
			removeLayer(this);
		}
		
		fillRectangle(0, 0, getWindowWidth(), getWindowHeight(), Color.WHITE);
		
		drawTexture(
				(getWindowWidth() - logo.getWidth()) / 2,
				(getWindowHeight()/2 - logo.getHeight()) / 2,
				logo, 0, 0,
				null, Direction.UP);
		
		JobManager<ModuleJob> manager = CrystalFarmLauncher.getJobManager();
		int total = manager.getJobs().size();
		int left = manager.getJobsLeft().size();
		
		char[] chars = ((total - left) + " / " + total).toCharArray();
		font.render(chars,
				(getWindowWidth() - font.getLength(chars, true)) / 2,
				(getWindowHeight()/2 + BAR_MARGIN_Y) - font.getHeight(),
				true, FontStyle.PLAIN, MAIN_COLOR);
		
		fillRectangle(
				BAR_MARGIN_X,
				getWindowHeight()/2 + BAR_MARGIN_Y,
				getWindowWidth() - BAR_MARGIN_X*2,
				BAR_HEIGHT,
				MAIN_COLOR);
		
		float coeff = left / (float) total;
		fillRectangle(
				(int) ((getWindowWidth() - BAR_MARGIN_X*2 - 2*BAR_THICKNESS) * (1 - coeff)) + BAR_MARGIN_X + BAR_THICKNESS + 1,
				getWindowHeight()/2 + BAR_MARGIN_Y + BAR_THICKNESS,
				(int) ((getWindowWidth() - BAR_MARGIN_X*2 - 2*BAR_THICKNESS) * coeff),
				BAR_HEIGHT - 2*BAR_THICKNESS,
				Color.WHITE);
		
		int y = getWindowHeight()/2 + BAR_HEIGHT + BAR_MARGIN_Y*2;
		
		for (ModuleJob job : current.values()) {
			font.render(job == null ? "Idling" : job.getName(),
					TABLE_MARGIN_X, y += font.getHeight(),
					true, FontStyle.PLAIN,
					MAIN_COLOR);
			font.render(job == null ? "Waiting for available jobs" : job.getDescription(),
					TABLE_MARGIN_X*2, y += font.getHeight(),
					false, FontStyle.PLAIN,
					MAIN_COLOR);
		}
		
	}

	@Override
	public void onInput(Input input) {
		input.consume();
	}

	@Override
	public void onJobAdded(JobManager<? extends ModuleJob> manager, ModuleJob job) {
		// Do nothing
	}

	@Override
	public void onJobStarted(JobManager<? extends ModuleJob> manager, ModuleJob job, Thread worker) {
		current.put(worker, job);
	}

	@Override
	public void onJobDone(JobManager<? extends ModuleJob> manager, ModuleJob job, Thread worker) {
		current.put(worker, null);
	}

	@Override
	public void onJobsBegin(JobManager<? extends ModuleJob> manager, int threads) {
		// Do nothing
	}

	@Override
	public void onJobsEnd(JobManager<? extends ModuleJob> manager) {
		removeLayer(this);
		current.clear();
	}

	@Override
	public void onJobDependencyProblem(JobManager<? extends ModuleJob> manager, Thread worker) {
		// Do nothing
	}

	@Override
	public void onJobErrored(JobManager<? extends ModuleJob> manager, Thread worker, ModuleJob job,
			Exception exception) {
		// Do nothing
	}

}
