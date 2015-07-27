# Snake

A Clojure "Snake" game. The rules are simple - Eat as many green food pellets as possible while not running into 
yourself. Use the arrow keys to move your snake. Your score is your snake length. If you self-collide you will reset.

## Building the Game

This app can be run in multiple modes:

* A precompiled Java application packaged as a standalone jar file. 
To build it type: *lein do clean, compile, uberjar*. Your executable will be found in the target folder.

* A lein runnable application. Simply type *lein run*.

* A JavaScript application. Type *lein cljsbuild once* to compile the js file. Now, open resources/public/index.html.

## License

Copyright © 2015 Mark Bastian

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
