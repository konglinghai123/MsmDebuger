/*
 * Copyright (c) 2015 张涛.
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
package com.dawnlightning.msmdebuger.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 联系人javabean
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class Contact implements Comparable<Contact>,Serializable {
    private String name;
    private int id;
    private String number;
    private String pinyin;
    private char firstChar;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        if (!TextUtils.isEmpty(pinyin)){
            String first = pinyin.substring(0, 1);
            if (first.matches("[A-Za-z]")) {
                firstChar = first.toUpperCase().charAt(0);
            } else {
                firstChar = '#';
            }
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getFirstChar() {
        return firstChar;
    }

    @Override
    public int compareTo(Contact another) {
        return this.pinyin.compareTo(another.getPinyin());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            return this.id == ((Contact) o).getId();
        } else {
            return super.equals(o);
        }
    }
}
