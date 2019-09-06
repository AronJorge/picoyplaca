package com.picoandplaca.model

data class DataHistory (val dateTimeRegistry:String,
                        val dateCont:String,
                        val timeCont:String,
                        val vehicle:String,
                        val contraventions:Boolean,
                        val disability:Boolean,
                        val sr:Boolean)