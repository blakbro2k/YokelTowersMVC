# YokelTowersMVC
Working on a clone for Yahoo Towers. Most update and uses Libgdx Autum for the UI

# Introduction
Greetings! Welcome to my Github.  This has been a pet project of my for at the better part of
5 years now.  In 1999, Yahoo.com created a fun hub of my childhood known as Yahoo! Games.
While they outsourced a lot of the games, this was one of their few in house products.
Now that Yahoo! Games is shutdown, this was removed with it.  I am trying to recreate it.

## Yahoo Towers
Click below to see the game
https://www.youtube.com/watch?v=cACEEUhb4e8&fbclid=IwAR1WHaezKc8820rHHLt2fPZaxezysNWehDhVghMNWsqUkLMMyRML1U0g3mA&ab_channel=CinnamorollB

### News & Community
What has been done so far.

I have all but completed the server launcher for this game which I built from scratch.
It has all the rooms, games and tables that report back to the client.

I am now working on the UI from scratch and finalizing the game logic.

The GameManager class is comming along nicely.  It can take any game from memory and update the game state for all 8 players properly.
There are still some issues with the game state, but right now, I would like to get an alpha playable.  So here is what I am doing:

I have created the start next game dialog box that properly counts down, then starts the game timer.
I need to finish the join box, that sits you at a table, and then displays 'Ready' if there is at least 1 other player seated.  It should 
display 'Waiting for players' when it is done.

There is a gameover message that displays when the game is over, and the game manager can correctly detect who wins.

This is the hard part, The GameManager class, has a simulated game, to handle the client side prediction.  I just had to try to recreate a multiplayer game,
8 clients at that!

https://www.gabrielgambetta.com/client-server-game-architecture.html has more information on what is needed to handle multiplayer games.

If I can get this, then the game is at least playable.

Animations are not done yet, blocks don't break they just disappear.
And Yahoos aren't implemented yet.

### Getting Started

The game is not playable yet as work through the UI.  If you wish to look at what I have done you may copy the git to your ide.
I use Android Studio and Gradle so it would be much easier if you use that.

To launch the main class, there is a DesktopLauncher class that will kick everything off.

### Contributing
Right now I am pretty much working solo.  I could use some reviewers if anyone has a better way of doing any of what I have done.

### Reporting Issues
Because the editor is now open source, you can use the [issue tracker](https://github.com/blakbro2k/YokelTowersMVC/issues?q=is%3Aopen) here on GitHub to report issues.

### License
YokelTowersMVC is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you
can use it free of charge, without strings attached in commercial and non-commercial projects.
