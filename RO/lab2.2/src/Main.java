
public class Main {
    public static void main(String[] args) throws Exception {
        GunShopDAO gunShop = new GunShopDAO();

        KnifeType comedy = new KnifeType("Comedy");
        KnifeType horror = new KnifeType("Horror");
        KnifeType fantasy = new KnifeType("Fantasy");

        gunShop.addKnifeType(comedy);
        gunShop.addKnifeType(horror);
        gunShop.addKnifeType(fantasy);

        Knife knife1 = new Knife("Dumb & Dumber", 2.2f, comedy);
        Knife knife2 = new Knife("Friends", 2f, comedy);
        Knife knife3 = new Knife("Ha-Ha", 1.45f, comedy);
        Knife knife4 = new Knife("Friday 13", 1.9f, horror);
        Knife knife5 = new Knife("Harry Potter", 1.5f, fantasy);

        gunShop.addKnife(knife1);
        gunShop.addKnife(knife2);
        gunShop.addKnife(knife3);
        gunShop.addKnife(knife4);
        gunShop.addKnife(knife5);

        Knife knife = new Knife("Extra knife", 5f, new KnifeType("Horror"));
        gunShop.addKnife(knife);

        knife = gunShop.getKnifeByName("Extra knife");
        Knife newKnife = new Knife("RENAMED", 2f, gunShop.getKnifeTypeById(gunShop.getKnifeTypeId("Fantasy")));

        gunShop.updateKnife(knife, newKnife);

        gunShop.deleteKnife("RENAMED");

        gunShop.deleteKnifeType("Comedy");

        gunShop.showKnifeTypes();
        gunShop.showKnives();

        gunShop.stop();
    }
}
