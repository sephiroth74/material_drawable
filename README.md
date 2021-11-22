# material_drawable
Provides material background drawables to views

[![Release](https://jitpack.io/v/sephiroth74/material_drawable.svg)](https://jitpack.io/#sephiroth74/material_drawable)


# Installation

Top level **build.gradle** file:

    buildscript {
        ...
    }

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }


The, inside your module **build.gradle** file:

    dependencies {
        ...
        implementation 'com.github.sephiroth74:material_drawable:**version**'
    }


# Usage

        val view1 = findViewById(R.id.testView1)

        view1.background = MaterialBackgroundDrawable.Builder(MaterialShape.Type.ALL) {
            focused(MaterialShapeDrawable.Style {
                color(Color.YELLOW)
            })
            pressed(MaterialShapeDrawable.Style {
                color(Color.GREEN)
            })
            normal(MaterialShapeDrawable.Style {
                color(Color.BLACK)
                strokeWidth(2f)
                style(Paint.Style.STROKE)
            })
        }.build()

        view1.setOnClickListener { Log.v("test", "onclick") }
