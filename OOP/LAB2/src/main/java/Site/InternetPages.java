package Site;

import java.util.ArrayList;
import java.util.List;

public class InternetPages {
    private List<Site> siteList;

    public InternetPages() {
        this.siteList = new ArrayList<Site>();
    }

    public InternetPages(List<Site> siteList) {
        this.siteList = siteList;
    }

    public List<Site> getSiteList() {
        if (siteList == null) {
            siteList = new ArrayList<Site>();
        }
        return siteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    public String output() {
        StringBuilder result = new StringBuilder();
        for (Site site : siteList) {
            result.append("title: ").append(site.getTitle()).append("\nsite type: ").append(site.getSiteType()).append("\nauthorize: ").append(site.isAuthorize()).append("\nemail: ").append(site.getChars().getEmail()).append("\npoll: ").append(site.getChars().isPoll()).append("\npoll type: ").append(site.getChars().getPollType()).append("\nfree: ").append(site.getChars().isFree()).append("\n#####\n");
        }
        return result.toString();
    }
}
