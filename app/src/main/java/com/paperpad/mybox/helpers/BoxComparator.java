/**
 * 
 */
package com.paperpad.mybox.helpers;

import com.paperpad.mybox.models.MyBox;

import java.util.Comparator;

/**
 * @author euphordev02
 *
 */
public class BoxComparator implements Comparator<MyBox> {

	/**
	 * 
	 */
	public BoxComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(MyBox lhs, MyBox rhs) {
		if (lhs.getPrix() > rhs.getPrix()) {
			return 1;
		}else if (lhs.getPrix() < rhs.getPrix()) {
			return -1;
		}
		return 0;
	}

}
