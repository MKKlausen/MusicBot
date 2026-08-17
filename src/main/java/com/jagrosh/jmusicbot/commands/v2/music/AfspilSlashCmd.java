package com.jagrosh.jmusicbot.commands.v2.music;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.v2.MusicSlashCommand;
import com.jagrosh.jmusicbot.commands.v2.SlashOutputAdapters.InteractionHookOutputAdapter;
import com.jagrosh.jmusicbot.commands.v2.SlashOutputAdapters.SlashEventOutputAdapter;
import com.jagrosh.jmusicbot.service.MusicService;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AfspilSlashCmd extends MusicSlashCommand {
    
    private final String loadingEmoji;
    private final MusicService musicService;

    private HashMap<String, String> playlists = new HashMap<String, String>() {{
            put("Mystik".toLowerCase(), "https://www.youtube.com/watch?v=dedMP7eda2E&list=PLJ4eaAy07RTulJe6LEAqfKlX46xgBo0JJ");
            put("Combat".toLowerCase(), "https://www.youtube.com/watch?v=ij9WUurfstg&list=PLJ4eaAy07RTvh3wx-yhyQH1Qilh1ZVl84");
            put("TPK".toLowerCase(), "https://www.youtube.com/watch?v=U-iHnbPb60Y&list=PL-MQlYeAlINBdWZqR1sWYU3SkNvIdoUTl");
            put("Ghustus".toLowerCase(), "https://www.youtube.com/watch?v=a0rNLdiKqpQ&list=PL-MQlYeAlINCiMfwud5uoCCS6uaXNdvbq");
            put("Feely".toLowerCase(), "https://www.youtube.com/watch?v=Bjjl2cPpemo&list=PL-MQlYeAlINBxFETORw_ug0erK8yz8JaJ");
            put("Action".toLowerCase(), "https://www.youtube.com/watch?v=BwQSnlYJax8&list=PL-MQlYeAlINCfTLNBeIVg8Yo_RIZ51B8J");
            put("Magic".toLowerCase(), "https://www.youtube.com/watch?v=hsAtH6fVEZE&list=PL-MQlYeAlINAvnP6lnc8mZ2EO2Sx2GiYn");
            put("Rivelia".toLowerCase(), "https://www.youtube.com/watch?v=erENuOJ5wQU&list=PL-MQlYeAlINCzz31VtYEYSwZKFBCq4MBp");
            put("Pantheon".toLowerCase(), "https://www.youtube.com/watch?v=YFwt8Abu-Xk&list=PL-MQlYeAlINCdhWjOtCm9RlQm8ACg73sf");
            put("Averia".toLowerCase(), "https://www.youtube.com/watch?v=0mmyZ4Y6NkM&list=PL-MQlYeAlINAxnzO7lyDtP6tvP_jmCRvT");
            put("BigSpook".toLowerCase(), "https://www.youtube.com/watch?v=Gbr6s0cbk5M&list=PL-MQlYeAlINCS5rUYakP1RwpDpd-jIy_K");
            put("TravelForrest".toLowerCase(), "https://www.youtube.com/watch?v=s5sTI_zBg40&list=PL-MQlYeAlIND8lKoA6_ifo1du5CAoyzF6");
            put("PirateAmb".toLowerCase(), "https://www.youtube.com/watch?v=0P5QCHHY1kw&list=PL-MQlYeAlINC0JziDFh6pefP1myjnTU4d");
            put("Monk".toLowerCase(), "https://www.youtube.com/watch?v=0PCk6kXbtF0&list=PL-MQlYeAlINCVAUJp53YpR7kgMh-UGLh0");
            put("Investigation".toLowerCase(), "https://www.youtube.com/watch?v=LsEOt0lNN6Q&list=PL-MQlYeAlINCcdAZGhXF2oXcZ5t7Ryz9Q");
            put("Socialite".toLowerCase(), "https://www.youtube.com/watch?v=ZUd1_PFKRAI&list=PL-MQlYeAlINBSk0Lf5K-DvGSVoatDXx_y");
            put("Stealth".toLowerCase(), "https://www.youtube.com/watch?v=4VfghQPbb3A&list=PL-MQlYeAlINA5df8FzuQ7_QgNnLOvwRsk");
            put("Chase".toLowerCase(), "https://www.youtube.com/watch?v=X2QjMpTdkQE&list=PL-MQlYeAlINACcf80PqrETG8oknl21aHa");
            put("Khestra".toLowerCase(), "https://www.youtube.com/watch?v=ulriP5SBStY&list=PL-MQlYeAlINB0iCSttk66gdfeVBUvB-yT");
            put("PirateComb".toLowerCase(), "https://www.youtube.com/watch?v=8eicrwA6CU4&list=PL-MQlYeAlINB8JcpAcMaDGSCEdKOK0Q4X");
            put("STT".toLowerCase(), "https://www.youtube.com/watch?v=pfA5UqEU_80&list=PL-MQlYeAlINBG52VJjcdabIZwlz0hGSLm");
            put("BCT".toLowerCase(), "https://www.youtube.com/watch?v=l2GIxY29_6o&list=PL-MQlYeAlINB5j3iu16MASEwbscA-7Zuv");
            put("Cozy".toLowerCase(), "https://www.youtube.com/watch?v=5W1KdR9Dhbc&list=PL-MQlYeAlINDpm2vW7p55ZuW0ltOpv3nJ");
            put("Suspense".toLowerCase(), "https://www.youtube.com/watch?v=Bk6xtTEEM1c&list=PL-MQlYeAlINA6CLqReBJrpnUD55ngLFq-");
            put("Hammerfall".toLowerCase(), "https://www.youtube.com/watch?v=E66Q7Tp9CeI&list=PL-MQlYeAlINDi6QjGFaUVA-eEs-XvGb5p");
            put("Recap".toLowerCase(), "https://www.youtube.com/watch?v=PQrAUVkAymE&list=PL-MQlYeAlINBdOymHwPYx2_dY0yInp6Ej");
            put("FoG".toLowerCase(), "https://www.youtube.com/watch?v=hVEkZnUYtKU&list=PL-MQlYeAlINBX4Zm78_NIggOUkc-ghsz2");
            put("FoE".toLowerCase(), "https://www.youtube.com/watch?v=TnkEXSCC3tU&list=PL-MQlYeAlINCp4XUqETja_7olo5myUpRd");
            put("Bouria".toLowerCase(), "https://www.youtube.com/watch?v=erENuOJ5wQU&list=PL-MQlYeAlINA5Y24IN-2ILcKJsYioNTWU");
            put("Heist".toLowerCase(), "https://www.youtube.com/watch?v=FkdR-GEm5AU&list=PL-MQlYeAlINCFG7-VDo8ixC5Ub-0XgJaz");
            put("Desert".toLowerCase(), "https://www.youtube.com/watch?v=94r_4et_220&list=PL-MQlYeAlINDYpa4ENW0zxs2YyU7wsORk");
            put("Western".toLowerCase(), "https://www.youtube.com/watch?v=UL16gE10Clc&list=PL-MQlYeAlINCmrhSe_yJatRPOyjd92MDm");
            put("Khaspia".toLowerCase(), "https://www.youtube.com/watch?v=PAHG-9uKUug&list=PL-MQlYeAlINDx7FVbwDv_JsFQj3jvhs6o");
            put("Orcs".toLowerCase(), "https://www.youtube.com/watch?v=ol9gYe_C8GI&list=PL-MQlYeAlINDrQeBCL3_J4se8TX0U8bTF");
            put("OrcsCombat".toLowerCase(), "https://www.youtube.com/watch?v=OK0L1MhMlYY&list=PL-MQlYeAlINCXD8cLtNfFFh3nUyTVgg_L");
        }};


    public AfspilSlashCmd(Bot bot)
    {
        super(bot);
        this.musicService = bot.getMusicService();
        this.loadingEmoji = bot.getConfig().getLoading();
        this.name = "afspil";
        this.help = "plays the provided song";

        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "playlistname", "navn på playliste", false).setAutoComplete(true));
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = false;
        this.bePlaying = false;
    }

    @Override
    public void doCommand(SlashCommandEvent event)
    {
        if (event.getOption("query") == null)
        {
            musicService.play(event.getGuild(), event.getMember(), "", event.getTextChannel(),
                    new SlashEventOutputAdapter(event));
            return;
        }

        String args = event.getOption("query").getAsString();
        event.reply(loadingEmoji + " Loading... `[" + args + "]`").queue(hook -> {
            musicService.play(event.getGuild(), event.getMember(), args, event.getTextChannel(),
                    new InteractionHookOutputAdapter(hook, event.getJDA(), event.getClient().getWarning()));
        });
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event)
    {
        String input = event.getFocusedOption().getValue();
        if(input.isEmpty())
        {
            event.replyChoices().queue();
            return;
        }

        if(isUrlOrPath(input))
        {
            event.replyChoices(new Command.Choice(input, input)).queue();
            return;
        }

        bot.getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:" + input,
                bot.getAudioLoadWrapper().wrap("ytsearch:" + input, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                String title = track.getInfo().title;
                event.replyChoices(new Command.Choice(truncateTitle(title), track.getInfo().uri)).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                event.replyChoices(buildChoicesFromPlaylist(playlist)).queue();
            }

            @Override
            public void noMatches()
            {
                event.replyChoices().queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                event.replyChoices().queue();
            }
        }));
    }

    /**
     * Checks if the input looks like a URL or file path (skip searching in that case).
     */
    private static boolean isUrlOrPath(String input)
    {
        return input.startsWith("http://") || input.startsWith("https://")
                || input.contains(":\\") || input.startsWith("/") || input.contains("\\");
    }

    /**
     * Builds autocomplete choices from a playlist, limited to 10 results.
     * Truncates titles longer than 100 characters (Discord's limit).
     */
    private static List<Command.Choice> buildChoicesFromPlaylist(AudioPlaylist playlist)
    {
        List<Command.Choice> choices = new ArrayList<>();
        int limit = Math.min(playlist.getTracks().size(), 10);
        for(int i = 0; i < limit; i++)
        {
            AudioTrack track = playlist.getTracks().get(i);
            String title = track.getInfo().title;
            choices.add(new Command.Choice(truncateTitle(title), track.getInfo().uri));
        }
        return choices;
    }

    /**
     * Truncates a title to fit Discord's 100 character limit for choice names.
     */
    private static String truncateTitle(String title)
    {
        if(title.length() > 100)
            return title.substring(0, 97) + "...";
        return title;
    }
}
