/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2014 Ausenco Engineering Canada Inc.
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
 */
package com.jaamsim.BasicObjects;

import java.util.ArrayList;

import com.jaamsim.Graphics.DisplayEntity;
import com.jaamsim.input.EntityInput;
import com.jaamsim.input.Keyword;

public class AddTo extends Pack {

	@Keyword(description = "The queue in which the waiting containers will be placed.",
	         exampleList = {"Queue1"})
	private final EntityInput<Queue> containerQueue;

	{
		prototypeEntityContainer.setHidden(true);

		containerQueue = new EntityInput<>(Queue.class, "ContainerQueue", "Key Inputs", null);
		containerQueue.setRequired(true);
		this.addInput(containerQueue);
	}

	@Override
	public void addEntity(DisplayEntity ent) {

		// Add an incoming container to its queue
		if (ent instanceof EntityContainer)
			containerQueue.getValue().addEntity(ent);
		else
			waitQueue.getValue().addEntity(ent);
	}

	@Override
	public ArrayList<Queue> getQueues() {
		ArrayList<Queue> ret = new ArrayList<>();
		ret.add(waitQueue.getValue());
		ret.add(containerQueue.getValue());
		return ret;
	}

	@Override
	protected EntityContainer getNextContainer() {
		return (EntityContainer) containerQueue.getValue().removeFirst();
	}

	@Override
	public void startAction() {

		// Is there a container waiting to be filled?
		if (container == null && containerQueue.getValue().getCount() == 0) {
			this.setBusy(false);
			this.setPresentState();
			return;
		}

		super.startAction();
	}

}
