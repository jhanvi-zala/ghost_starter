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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;


    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int x = binarysearch(prefix);
        if(x==-1)
        {
            return null;
        }
        else
        {
            return (words.get(x));

        }

    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        int indexvalue = binarysearch(prefix);
        int upindex,downindex;
        upindex=indexvalue;
        downindex=indexvalue;

        int checkword;
        ArrayList<String> storeevenwords = new ArrayList<String>();
        while (true)
        {
            upindex--;
            if(upindex == 0)
            {
                return null;
            }
            checkword = words.get(upindex).startsWith(prefix)?0:prefix.compareTo(words.get(upindex));
            if(checkword!=0)
            {
                return null;
            }
            else
            {
                if(words.get(upindex).length()%2==0)
                {
                    storeevenwords.add(words.get(upindex));
                }
            }
        }
        while (true)
        {
            downindex++;
            if(downindex == words.size())
            {
                return null;
            }
            checkword = words.get(downindex).startsWith(prefix)?0:prefix.compareTo(words.get(upindex));
            if(checkword!=0)
            {
                return null;
            }
            else
            {
                if(words.get(upindex).length()%2==0)
                {
                    storeevenwords.add(words.get(upindex));
                }
            }
        }



       // return selected;
    }

    public int binarysearch(String prefix)
    {
        int lower=0;
        int higher = words.size();
        int middle;
        String checkWord;
        int checklist=0;
        while(lower<higher)
        {
            middle = (lower+higher)/2;
            checkWord = words.get(middle);
            checklist = checkWord.startsWith(prefix)?0:prefix.compareTo(checkWord);
            if(checklist == 0)
            {
                return middle;
            }
            else if(checklist>0)
            {
                lower = middle +1;
            }
            else
            {
                higher=middle;
            }
        }

        return -1;
    }
}
