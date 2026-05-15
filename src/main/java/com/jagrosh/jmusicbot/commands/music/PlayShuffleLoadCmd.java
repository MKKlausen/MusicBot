/*
 * Copyright 2016 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.audio.RequestMetadata;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.playlist.PlaylistLoader.Playlist;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.jagrosh.jmusicbot.utils.TimeUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dev.lavalink.youtube.track.YoutubeAudioTrack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.jagrosh.jmusicbot.queue.AbstractQueue;

/**
 *
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class PlayShuffleLoadCmd extends MusicCommand
{
    
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
    }};


        
    private final String loadingEmoji;
    
    public PlayShuffleLoadCmd(Bot bot)
    {
        super(bot);
        this.loadingEmoji = bot.getConfig().getLoading();
        this.name = "afspil";
        this.arguments = "<title|URL|subcommand>";
        this.help = "plays the provided playlist, loads it and shuffles.";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = false;
        this.children = new Command[]{new PlaylistCmd(bot)};
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        if(event.getArgs().isEmpty() && event.getMessage().getAttachments().isEmpty())
        {
            AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
            if(handler.getPlayer().getPlayingTrack()!=null && handler.getPlayer().isPaused())
            {
                if(DJCommand.checkDJPermission(event))
                {
                    handler.getPlayer().setPaused(false);
                    event.replySuccess("Resumed **"+handler.getPlayer().getPlayingTrack().getInfo().title+"**.");
                }
                else
                    event.replyError("Only DJs can unpause the player!");
                return;
            }
            StringBuilder builder = new StringBuilder(event.getClient().getWarning()+" Play Commands:\n");
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" <song title>` - plays the first result from Youtube");
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" <URL>` - plays the provided song, playlist, or stream");
            for(Command cmd: children)
                builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName()).append(" ").append(cmd.getArguments()).append("` - ").append(cmd.getHelp());
            event.reply(builder.toString());
            return;
        }

        

        String args;

        if (playlists.get(event.getArgs().toLowerCase()) != null){
            args = playlists.get(event.getArgs());
        } else {
            args = event.getArgs().startsWith("<") && event.getArgs().endsWith(">") 
                ? event.getArgs().substring(1,event.getArgs().length()-1) 
                : event.getArgs().isEmpty() ? event.getMessage().getAttachments().get(0).getUrl() : event.getArgs(); 
        }
            
;
        event.reply(loadingEmoji+" Loading... `["+args+"]`", m -> bot.getPlayerManager().loadItemOrdered(event.getGuild(), args, new ResultHandler(m,event,false)));
    }
    
    private class ResultHandler implements AudioLoadResultHandler
    {
        private final Message m;
        private final CommandEvent event;
        private final boolean ytsearch;
        
        private ResultHandler(Message m, CommandEvent event, boolean ytsearch)
        {
            this.m = m;
            this.event = event;
            this.ytsearch = ytsearch;
        }
        
        private void loadSingle(AudioTrack track, AudioPlaylist playlist)
        {
            if(bot.getConfig().isTooLong(track))
            {
                m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" This track (**"+track.getInfo().title+"**) is longer than the allowed maximum: `"
                        + TimeUtil.formatTime(track.getDuration())+"` > `"+ TimeUtil.formatTime(bot.getConfig().getMaxSeconds()*1000)+"`")).queue();
                return;
            }

            
            AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
            AudioPlayer player = handler.getPlayer();

            if (player.getPlayingTrack()!= null) {
                player.stopTrack();
                
            }
            
            handler.getQueue().clear();
            
            loadPlaylist(playlist, null);

            
            AudioTrack currentTrack = handler.getPlayer().getPlayingTrack();
            
            String addMsg = FormatUtil.filter(event.getClient().getSuccess()+" Added **"+currentTrack.getInfo().title
                    +"** (`"+ TimeUtil.formatTime(currentTrack.getDuration())+"`) ");
            
            
            

            if(playlist==null || !event.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION))
                m.editMessage(addMsg).queue();
            else
            {
                m.editMessage(addMsg+"\n"+event.getClient().getSuccess()+" Loaded **"+playlist.getName()+"!").queue();   
            }

            
        }
        
        private int loadPlaylist(AudioPlaylist playlist, AudioTrack exclude)
        {
            Collections.shuffle(playlist.getTracks());
            int[] count = {0};
            playlist.getTracks().stream().forEach((track) -> {
                if(!bot.getConfig().isTooLong(track) && !track.equals(exclude))
                {
                    AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
                    handler.addTrack(new QueuedTrack(track, RequestMetadata.fromResultHandler(track, event)));
                    count[0]++;
                }
            });
            return count[0];
        }
        
        @Override
        public void trackLoaded(AudioTrack track)
        {
            loadSingle(track, null);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist)
        {
            if(playlist.getTracks().size()==1 || playlist.isSearchResult())
            {
                AudioTrack single = playlist.getSelectedTrack()==null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
                loadSingle(single, null);
            }
            else if (playlist.getSelectedTrack()!=null)
            {
                AudioTrack single = playlist.getSelectedTrack();
                loadSingle(single, playlist);
            }
            else
            {
                int count = loadPlaylist(playlist, null);
                if(playlist.getTracks().size() == 0)
                {
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" The playlist "+(playlist.getName()==null ? "" : "(**"+playlist.getName()
                            +"**) ")+" could not be loaded or contained 0 entries")).queue();
                }
                else if(count==0)
                {
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" All entries in this playlist "+(playlist.getName()==null ? "" : "(**"+playlist.getName()
                            +"**) ")+"were longer than the allowed maximum (`"+bot.getConfig().getMaxTime()+"`)")).queue();
                }
                else
                {
                    m.editMessage(FormatUtil.filter(event.getClient().getSuccess()+" Found "
                            +(playlist.getName()==null?"a playlist":"playlist **"+playlist.getName()+"**")+" with `"
                            + playlist.getTracks().size()+"` entries; added to the queue!"
                            + (count<playlist.getTracks().size() ? "\n"+event.getClient().getWarning()+" Tracks longer than the allowed maximum (`"
                            + bot.getConfig().getMaxTime()+"`) have been omitted." : ""))).queue();
                }
            }
        }

        @Override
        public void noMatches()
        {
            if(ytsearch)
                m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" No results found for `"+event.getArgs()+"`.")).queue();
            else
                bot.getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:"+event.getArgs(), new ResultHandler(m,event,true));
        }

        @Override
        public void loadFailed(FriendlyException throwable)
        {
            if(throwable.severity==Severity.COMMON)
                m.editMessage(event.getClient().getError()+" Error loading: "+throwable.getMessage()).queue();
            else
                m.editMessage(event.getClient().getError()+" Error loading track.").queue();
        }
    }
    
    public class PlaylistCmd extends MusicCommand
    {
        public PlaylistCmd(Bot bot)
        {
            super(bot);
            this.name = "playlist";
            this.aliases = new String[]{"pl"};
            this.arguments = "<name>";
            this.help = "plays the provided playlist";
            this.beListening = true;
            this.bePlaying = false;
        }

        @Override
        public void doCommand(CommandEvent event) 
        {
            if(event.getArgs().isEmpty())
            {
                event.reply(event.getClient().getError()+" Please include a playlist name.");
                return;
            }
            Playlist playlist = bot.getPlaylistLoader().getPlaylist(event.getArgs());
            if(playlist==null)
            {
                event.replyError("I could not find `"+event.getArgs()+".txt` in the Playlists folder.");
                return;
            }
            event.getChannel().sendMessage(loadingEmoji+" Loading playlist **"+event.getArgs()+"**... ("+playlist.getItems().size()+" items)").queue(m -> 
            {
                AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
                playlist.loadTracks(bot.getPlayerManager(), (at)->handler.addTrack(new QueuedTrack(at, RequestMetadata.fromResultHandler(at, event))), () -> {
                    StringBuilder builder = new StringBuilder(playlist.getTracks().isEmpty() 
                            ? event.getClient().getWarning()+" No tracks were loaded!" 
                            : event.getClient().getSuccess()+" Loaded **"+playlist.getTracks().size()+"** tracks!");
                    if(!playlist.getErrors().isEmpty())
                        builder.append("\nThe following tracks failed to load:");
                    playlist.getErrors().forEach(err -> builder.append("\n`[").append(err.getIndex()+1).append("]` **").append(err.getItem()).append("**: ").append(err.getReason()));
                    String str = builder.toString();
                    if(str.length()>2000)
                        str = str.substring(0,1994)+" (...)";
                    m.editMessage(FormatUtil.filter(str)).queue();
                });
            });
        }
    }

}
