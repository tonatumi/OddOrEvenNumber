package practice.tonatumi.oddorevennumber;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public final class OddOrEvenNumber extends JavaPlugin {
    boolean gameStats = false;
    int result = new Random().nextInt(6)+1;
    List<String> chooseOdd = new ArrayList<String>();
    List<String> chooseEven = new ArrayList<String>();
    int time = 30;
    String prefix ="Odd or Even";

    private OddOrEvenNumber plugin;





    public void showHelp(Player p){
        p.sendMessage("--------"+prefix+"---------");
        p.sendMessage("/oe create:新しくゲームを開始します。");
        p.sendMessage("/oe o:今開催されているゲームで偶数に賭けます");
        p.sendMessage("/oe e:今開催されているゲームで奇数に賭けます");
    }

    public void countDown(){
        new BukkitRunnable(){

            @Override
            public void run(){
                //ゲームが開催されていて
                if(gameStats){
                    //賭け時間が1秒以上あって
                    if(time > 0){
                        //残り時間が10の倍数か5秒以下になったら表示。
                        if(time%10==0 && time<=5){
                            Bukkit.getServer().broadcastMessage(prefix+":ダイスが振られるまであと"+time+"秒です。");


                        }
                    }
                    //時間いっぱいで賭けが成立しなかった場合
                    if(time==0){
                        if(!(chooseEven.size()>0 && chooseOdd.size()>0)){
                            Bukkit.getServer().broadcastMessage(prefix+":賭けが成立しないため流れました...。");
                            end();
                        }else{
                            Bukkit.getServer().broadcastMessage(prefix+":ダイスを振っています...");
                            game();

                        }
                        return;
                    }

                }else{
                    cancel();
                    return;
                }
                time --;
            }
        }.runTaskTimer(plugin,0,20);
    }

    public void end(){
        gameStats = false;
        time = 30;
        chooseEven.clear();
        chooseOdd.clear();
        Bukkit.getServer().broadcastMessage(prefix+":game over");

    }

    public void game(){
        //偶数の時(even)
        if(result%2==0){
            Bukkit.getServer().broadcastMessage(prefix+":"+result+"が出ました。偶数！");
            Bukkit.getServer().broadcastMessage(prefix+"：偶数の勝ち！");
            for(int i = 0; i< chooseEven.size(); i++){
                Bukkit.getServer().broadcastMessage(prefix+":"+ chooseEven.get(i) +"の勝ち！");
            }
        }else{//奇数の時(Odd)
            Bukkit.getServer().broadcastMessage(prefix+":"+result+"が出ました。奇数！");
            Bukkit.getServer().broadcastMessage(prefix+":奇数の勝ち！");
            for(int i=0; i< chooseOdd.size(); i++){
                Bukkit.getServer().broadcastMessage(prefix+":"+ chooseOdd.get(i) +"の勝ち！");
            }
        }
        end();

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        if (!(p instanceof Player)) {
            return false;
        }

        //oeコマンド
        if (args.length == 0) {
            showHelp(p);
            return true;
        }


        //コマンドに引数があるとき
        if (args.length == 1) {
            //create
            if (args[0].equalsIgnoreCase("create")) {
                //すでに始まっているとき
                if (gameStats) {
                    p.sendMessage(prefix+":すでに始まっています。");
                    return false;

                }

                Bukkit.getServer().broadcastMessage(prefix+":"+p.getDisplayName() + "さんが新しく丁半を始めました。");
                gameStats = true;
                countDown();
                return true;

            }

            //oe o 偶数に賭けるとき
            if (args[0].equalsIgnoreCase("O")) {
                //ゲームが開催されているかチェック
                if (!gameStats) {
                    p.sendMessage(prefix+":現在開催されていません。");
                    return false;

                }
                //丁に賭けてないかチェック
                if (chooseOdd.contains(p.getDisplayName())) {
                    p.sendMessage(prefix+":あなたはすでに丁に賭けています。");
                    return false;
                }
                //半に賭けてないかチェック
                if (chooseEven.contains(p.getDisplayName())) {
                    p.sendMessage(prefix+":あなたはすでに半に賭けています。");
                    return false;
                }
                //偶数に賭ける
                Bukkit.getServer().broadcastMessage(prefix+":"+p.getDisplayName() + "さんが丁に賭けました！");
                chooseOdd.add(p.getDisplayName());
                return true;
            }

            //oe e 奇数に賭けるとき
            if (args[0].equalsIgnoreCase("E")) {
                //開催チェック
                if (!gameStats) {
                    p.sendMessage(prefix+":現在開催されていません。");
                    return false;

                }

                //丁に賭けてないかチェック
                if (chooseOdd.contains(p.getDisplayName())) {
                    p.sendMessage(prefix+":あなたはすでに丁に賭けています。");
                    return false;
                }

                //半に賭けてないかチェック
                if (chooseEven.contains(p.getDisplayName())) {
                    p.sendMessage(prefix+":あなたはすでに半に賭けています。");
                    return false;
                }

                //奇数に賭ける
                Bukkit.getServer().broadcastMessage(prefix+":"+p.getDisplayName() + "さんが半に賭けました！");
                chooseEven.add(p.getDisplayName());
                return true;
            }


        }
        return false;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("oe").setExecutor(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
