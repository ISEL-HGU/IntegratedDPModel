import java.util.ArrayList;

public class DuplicateList {
	public static void main(String[] args){
		ArrayList<Integer> idxList = new ArrayList<Integer>();
		int[] num = {10, 10, 10, 20, 30, 30, 30};
		int duplicateNum = 0;
		boolean flag = false;
		for(int i = 0; i < num.length; i++) {
			if(num[i]  == duplicateNum) continue;
			for(int j = i + 1; j < num.length; j++) {
				
				if(num[i] == num[j] && flag == false ) {
					duplicateNum = num[i];
					idxList.add(i);
					idxList.add(j);
					flag = true;
				}
				else if(num[i] == num[j] && flag == true) {
					idxList.add(j);
				}
				else if(num[i] != num[j]) {
					continue;
					
				}
			}
			if(flag == true) {
				System.out.println("duplicateNum " + duplicateNum);
				for(int k = 0; k < idxList.size(); k++) {
					System.out.println(idxList.get(k));
					
				}	
			}
			flag = false;
			idxList.clear();
		}
	}
}
