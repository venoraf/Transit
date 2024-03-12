package transit;

import java.util.ArrayList;

import org.w3c.dom.Node;

import transit.TNode;

/**
 * This class contains methods which perform various operations on a layered
 * linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/*
	 * Default constructor used by the driver and Autolab.
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit() {
		trainZero = null;
	}

	/*
	 * Default constructor used by the driver and Autolab.
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) {
		trainZero = tz;
	}

	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero() {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations,
	 * bus
	 * stops, and walking locations. Each layer begins with a location of 0, even
	 * though
	 * the arrays don't contain the value 0. Store the zero node in the train layer
	 * in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops      Int array listing all the bus stops
	 * @param locations     Int array listing all the walking locations (always
	 *                      increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

		trainZero = new TNode(0);
		TNode busZero = new TNode(0);
		TNode walkZero = new TNode(0);
		trainZero.setDown(busZero);
		busZero.setDown(walkZero);

		TNode temp = trainZero;
		for (int i = 0; i < trainStations.length; i++) {
			TNode trainStation = new TNode(trainStations[i]);
			temp.setNext(trainStation);
			temp = trainStation;
		}

		TNode sunoo = trainZero.getNext();
		temp = busZero;
		for (int i = 0; i < busStops.length; i++) {
			TNode busStation = new TNode(busStops[i]);
			temp.setNext(busStation);
			if (sunoo != null && sunoo.getLocation() == busStation.getLocation()) {
				sunoo.setDown(busStation);
				sunoo = sunoo.getNext();
			}
			temp = busStation;
		}

		TNode jungwon = busZero.getNext();
		temp = walkZero;
		for (int i = 0; i < locations.length; i++) {
			TNode walkStop = new TNode(locations[i]);
			temp.setNext(walkStop);
			if (jungwon != null && jungwon.getLocation() == walkStop.getLocation()) {
				jungwon.setDown(walkStop);
				jungwon = jungwon.getNext();
			}
			temp = walkStop;
		}
	}

	/**
	 * Modifies the layered list to remove the given train station but NOT its
	 * associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {

		TNode sunoo = trainZero;

		while (sunoo != null) {
			if (sunoo.getNext() != null && sunoo.getNext().getLocation() == station) {
				sunoo.setNext(sunoo.getNext().getNext());
				break;
			}
			sunoo = sunoo.getNext();
		}

	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do
	 * nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		TNode heeseung = trainZero.getDown().getDown();
		int maxwalk = 0;
		while (heeseung != null) {
			maxwalk = heeseung.getLocation();
			heeseung = heeseung.getNext();
		}
		if (busStop > maxwalk ) return;
		heeseung = trainZero.getDown();
		TNode sunghoon = new TNode(busStop);

		while (heeseung != null) {
			if (heeseung.getNext() != null && heeseung.getNext().getLocation() > busStop
				&& heeseung.getLocation() != busStop) {
				sunghoon.setNext(heeseung.getNext());
				heeseung.setNext(sunghoon);
				heeseung = heeseung.getNext();
				sunghoon = trainZero.getDown().getDown();
				while (sunghoon != null) {
					if (sunghoon.getLocation() == heeseung.getLocation()) {
						heeseung.setDown(sunghoon);
						break;
					}
					sunghoon = sunghoon.getNext();
				}
				break;
			} else if (heeseung.getNext() == null && heeseung.getLocation() != busStop) {
				heeseung.setNext(sunghoon);
				sunghoon = trainZero.getDown().getDown();
				heeseung = heeseung.getNext();
				while (sunghoon != null) {
					if (sunghoon.getLocation() == heeseung.getLocation()) {
						heeseung.setDown(sunghoon);
						break;
					}
					sunghoon = sunghoon.getNext();
				}
				break;
			} else if (heeseung.getLocation() == busStop) {
				break;
			}
			heeseung = heeseung.getNext();
		}

	}

	/**
	 * Determines the optimal path to get to a given destination in the walking
	 * layer, and
	 * collects all the nodes which are visited in this path into an arraylist.
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

		TNode niki = trainZero;
		int jay = trainZero.getLocation();
		ArrayList<TNode> ret = new ArrayList<TNode>();
		while (niki != null) {
			if (niki.getLocation() <= destination) {
				ret.add(niki);
				jay = niki.getLocation();
			}
			niki = niki.getNext();
		}

		niki = trainZero.getDown();
		while (niki != null) {
			if (niki.getLocation() <= destination && niki.getLocation() >= jay) {
				ret.add(niki);
				jay = niki.getLocation();
			}
			niki = niki.getNext();
		}

		niki = trainZero.getDown().getDown();
		while (niki != null) {
			if (niki.getLocation() <= destination && niki.getLocation() >= jay) {
				ret.add(niki);
				jay = niki.getLocation();
			}
			niki = niki.getNext();
		}
		return ret;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the
	 * same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode jungwon = new TNode(0);
		TNode enhypen = trainZero;
		ArrayList<Integer> trainstations = new ArrayList<Integer>();

		while (enhypen != null) {
			enhypen = enhypen.getNext();
			if (enhypen != null) {
				trainstations.add(enhypen.getLocation());
			}
		}

		enhypen = trainZero.getDown();
		ArrayList<Integer> busstops = new ArrayList<Integer>();
		while (enhypen != null) {
			enhypen = enhypen.getNext();
			if (enhypen != null) {
				busstops.add(enhypen.getLocation());
			}
		}

		enhypen = trainZero.getDown().getDown();
		ArrayList<Integer> walkstops = new ArrayList<Integer>();
		while (enhypen != null) {
			enhypen = enhypen.getNext();
			if (enhypen != null) {
				walkstops.add(enhypen.getLocation());
			}
		}

		int[] trainint = new int[trainstations.size()];
		int[] busint = new int[busstops.size()];
		int[] walkint = new int[walkstops.size()];

		for (int i = 0; i < trainstations.size(); i++) {
			trainint[i] = trainstations.get(i);
		}

		for (int i = 0; i < busstops.size(); i++) {
			busint[i] = busstops.get(i);
		}

		for (int i = 0; i < walkstops.size(); i++) {
			walkint[i] = walkstops.get(i);
		}

		enhypen = trainZero;
		makeList(trainint, busint, walkint);
		jungwon = trainZero;
		trainZero = enhypen;

		return jungwon;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are
	 *                     located
	 */
	public void addScooter(int[] scooterStops) {

		TNode sunoo = trainZero.getDown();
		TNode sunghoon = trainZero.getDown().getDown();
		TNode scooterZero = new TNode(0);
		TNode jake = scooterZero;
		scooterZero.setDown(sunghoon);
		sunoo.setDown(jake);

		for (int i = 0; i < scooterStops.length; i++) {
			TNode temp = new TNode(scooterStops[i]);
			jake.setNext(temp);
			jake = temp;
			while (sunoo != null && sunoo.getLocation() <= jake.getLocation()) {
				if (sunoo != null && sunoo.getLocation() == jake.getLocation()) {
					sunoo.setDown(jake);
				}
				sunoo = sunoo.getNext();
			}

			while (sunghoon != null && sunghoon.getLocation() <= jake.getLocation()) {
				if (sunghoon != null && sunghoon.getLocation() == jake.getLocation()) {
					jake.setDown(sunghoon);
				}
				sunghoon = sunghoon.getNext();
			}
		}
	}

	/**
	 * Used by the driver to display the layered linked list.
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null)
					break;

				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation() + 1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++)
						StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null)
				break;
			StdOut.println();

			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation())
					downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr)
					StdOut.print("|");
				else
					StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen - 1; j++)
					StdOut.print(" ");

				if (horizPtr.getNext() == null)
					break;

				for (int i = horizPtr.getLocation() + 1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++)
							StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}

	/**
	 * Used by the driver to display best path.
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr))
					StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++)
						StdOut.print(" ");
				}
				if (horizPtr.getNext() == null)
					break;

				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation() + 1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);

					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++)
						StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}

			if (vertPtr.getDown() == null)
				break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen - 1; j++)
					StdOut.print(" ");

				if (horizPtr.getNext() == null)
					break;

				for (int i = horizPtr.getLocation() + 1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++)
							StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
