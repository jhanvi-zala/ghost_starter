/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.google.engedu.ghost.R.id.gameStatus;
import static com.google.engedu.ghost.R.id.ghostText;
import android.os.Handler;

public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private SimpleDictionary dictionary;
    private boolean userTurn ;
    private Random random = new Random();
    private String wordfragment = "";
    public TextView text;
    public Button challenge;
    public Button reset;
    public String computerWord;
    TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try{
            InputStream input = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(input);


        }catch (IOException e) {
            Toast.makeText(getApplicationContext(),"could not load ",Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        }
        challenge = (Button)findViewById(R.id.chlng);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(null);
            }
        });
        challenge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String st = dictionary.getAnyWordStartingWith(wordfragment);
                if(st==null)
                {
                    Toast.makeText(getApplicationContext(),"No such word !! you win",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),st+" exists!! computer wins",Toast.LENGTH_SHORT).show();
                }
            }

        });
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (!userTurn ) {
            label.setText(COMPUTER_TURN);
            computerTurn();

        } else {
            label.setText(USER_TURN);
        }
        return true;
    }

    private void computerTurn() {
        label = (TextView) findViewById(R.id.gameStatus);
        label.setText(COMPUTER_TURN);

        Handler han = new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(wordfragment.equals(""))
                {
                    char ch = 'a';
                    Random ran = new Random();
                    int c = ran.nextInt(25) + 1;
                    wordfragment = ""+ch+c;
                }
                else {
                    computerWord = dictionary.getAnyWordStartingWith(wordfragment);
                    System.out.println(computerWord);
                    if (computerWord == null) {
                        Toast.makeText(getApplicationContext(), "No such word !! computer wins", Toast.LENGTH_SHORT).show();
                    } else if (computerWord.equals(wordfragment)) {
                        Toast.makeText(getApplicationContext(), "you ended game , computer wins", Toast.LENGTH_SHORT).show();
                    } else {
                        wordfragment = computerWord.substring(0, wordfragment.length() + 1);
                        text.setText(wordfragment);
                        label.setText(USER_TURN);
                    }
                }

            }
        },1000);
        // Do computer turn stuff then make it the user's turn again

        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        char pressedkey = (char ) event.getUnicodeChar();
        pressedkey = Character.toLowerCase(pressedkey);
        if(pressedkey >= 'a' && pressedkey<= 'z' )
        {
            wordfragment = String.valueOf(text.getText());
            wordfragment+=pressedkey;

            text.setText(wordfragment);
            computerTurn();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"invalid char ", Toast.LENGTH_SHORT).show();
            return super.onKeyUp(keyCode, event);
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        return super.onKeyUp(keyCode, event);

    }
}
