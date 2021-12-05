import java.util.*;


public class PageFault {
    public static void replacePage(Vector<Page> mem, int replacePageNum, ControlPanel controlPanel) {
        Page resultPage = null;
        while (resultPage == null) {
            Page oldestPage = null;
            for (Page page : mem) {
                if (page.physical != -1) {
                    if (oldestPage == null) {
                        oldestPage = page;
                    }
                    if (page.inMemTime > oldestPage.inMemTime) {
                        oldestPage = page;
                    }
                }
            }
            if (oldestPage.R == 1) {
                oldestPage.inMemTime = 0;
                oldestPage.lastTouchTime = 0;
                oldestPage.R = 0;
            } else {
                resultPage = oldestPage;
            }
        }

        Page nextpage = mem.elementAt(replacePageNum);
        controlPanel.removePhysicalPage(resultPage.id);
        nextpage.physical = resultPage.physical;
        controlPanel.addPhysicalPage(nextpage.physical, replacePageNum);
        resultPage.inMemTime = 0;
        resultPage.lastTouchTime = 0;
        resultPage.R = 0;
        resultPage.M = 0;
        resultPage.physical = -1;
    }
}
