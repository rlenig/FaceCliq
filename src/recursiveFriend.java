import java.util.Scanner;

import android.util.Log;


public class recursiveFriend {

	int start, end, del,brack;
	String friendName;
	public recursiveFriend(){

	}

	public String[] getFriends(String input){
		String allFriends [] = null;

		if(input.contains("username")){
			//find first username
			start= input.indexOf("username");

			end = input.length();

			System.out.println(start);
			System.out.println(end);

			//leftover input
			//get just username
			String user = input.substring(start);
			del = user.lastIndexOf(":");
			brack = user.indexOf("}");
			user=user.substring(del,brack+1);

			input= input.substring(brack);
			getFriends(input);
		}


		return allFriends;
	}

}