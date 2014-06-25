package dom;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String name;
    private String gender;
    private Integer answerCount;
	private Integer gameCount;
	private Integer sumTime;
	private String fullName; //firstname SPACE lastname
	private String birthdate;

    public User() {}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
	
	public Integer getAnswerCount(){
		return answerCount;
	}
	
	public void setAnswerCount(Integer answerCount){
		this.answerCount = answerCount;
	}
	
	public Integer getGameCount(){
		return gameCount;
	}
	
	public void setGameCount(Integer gameCount){
		this.gameCount = gameCount;
	}
	
	public Integer getSumTime(){
		return sumTime;
	}
	
	public void setSumTime(Integer sumTime){
		this.sumTime = sumTime;
	}
	
	public String getFullName(){
		return fullName;
	}
	
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
	
	public String getBirthdate(){
		return birthdate;
	}
	
	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("User [");
        if (name != null) {
            sb.append("name=").append(name).append(", ");
        }
		if (fullName != null) {
            sb.append("fullName=").append(fullName).append(", ");
        }
        if (gender != null) {
            sb.append("gender=").append(gender).append(", ");
        }
        if (gameCount != null) {
            sb.append("gameCount=").append(gameCount).append(", ");
        }
		if (answerCount != null) {
            sb.append("answerCount=").append(answerCount).append(", ");
        }
        if (sumTime != null) {
            sb.append("sumTime=").append(sumTime);
        }

        sb.append("]");

        return sb.toString();
    }
}
