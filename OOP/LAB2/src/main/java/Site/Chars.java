package Site;

public class Chars {
    private String email;
    private boolean poll;
    private PollType pollType;
    private boolean free;

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPoll() {
        return poll;
    }

    public void setPoll(boolean poll) {
        this.poll = poll;
    }

    public PollType getPollType() {
        return pollType;
    }

    public void setPollType(PollType pollType) {
        this.pollType = pollType;
    }
}
