/*

Copyright (C) 2004 Thorsten Berger

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package de.thorstenberger.taskmodel.complex;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * random utils
 *
 * @author Thorsten Berger
 *
 */
public class RandomUtil {

	private static Random r = new Random();

	/**
	 * get random permutation of the n first natural numbers
	 * @param n
	 * @return array of length n containing the permutation
	 */
	public static int[] getPermutation( int n ){
		int[] ret = new int[n];

		int[] remaining = new int[n];
		// init remaining array with the first n natural numbers
		for(int i=0;i<remaining.length;i++) {
            remaining[i] = i;
        }

		// select random element and remove from remaining array
		for(int i=0;i<ret.length;i++){
			int a = r.nextInt( remaining.length );
			ret[i] = remaining [ a ];
			remaining = removeElement( remaining, a);
		}

		return ret;
	}

	/**
	 * removes one field from array
	 * @param array the array
	 * @param index field index to remove
	 * @return
	 */
	private static int[] removeElement( int[] array, int index ){
		int[] ret = new int[ array.length - 1 ];
		int destPos = 0;
		for( int i=0; i<array.length;i++){
			if( i!=index ) {
                ret[ destPos++ ] = array[i];
            }
		}
		return ret;
	}

	/**
	 * get a pseudorandom, uniformly distributed <tt>int</tt>
     * value between 0 (inclusive) and n (exclusive)
	 * @param n
	 * @return
	 */
	public static int getInt( int n ){
		return r.nextInt( n );
	}

	/**
	 * just testing
	 * @param args
	 */
	public static void main( String args[] ){
//		for(int i=0;i<100;i++){
//			int[] test = getPermutation( 49 );
//			System.out.println( test[0] + ", " + test[1] + ", " + test[2] + ", " + test[3] + ", " + test[4] + ", " + test[5] );
//		}

		int allSubtasksSize=10;
		int numOfTasks = 2;
		int[] selectOrder = new int[0];

		int[] tmpOrder = RandomUtil.getPermutation( allSubtasksSize );
		selectOrder = new int[ numOfTasks ];
		System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
		Arrays.sort( selectOrder );										// und sortieren

		for( int i=0; i<selectOrder.length;i++ ){
			System.out.print ( selectOrder[i] + " " );
		}
	}

    /**
     * Use "inside out" Durstenfeld-Shuffle for shuffling the given array. Returns a copy, does not change the given
     * array in place.
     *
     * @param source
     *            array to sort
     * @return shuffled array
     * @throws NullPointerException
     *             if array==null
     */
    public static int[] shuffle(int[] source) {
        int[] shuffled = new int[source.length];
        if (source.length == 0)
            return shuffled;
        Random rnd = new Random();

        shuffled[0] = source[0];
        for (int i = 1; i < source.length; i++) {
            int j = rnd.nextInt(i + 1);
            shuffled[i] = shuffled[j];
            shuffled[j] = source[i];
        }
        return shuffled;
    }

}
