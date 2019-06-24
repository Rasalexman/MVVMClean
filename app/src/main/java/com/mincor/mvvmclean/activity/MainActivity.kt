package com.mincor.mvvmclean.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.mincor.mvvmclean.view.GenresController
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

class MainActivity : AppCompatActivity(), ActionBarProvider {

    // главный роутер приложения
    private var mainRouter: Router? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // назначаем контейнер для приложения
        val container = frameLayout { lparams(matchParent, matchParent) }

        mainRouter = Conductor.attachRouter(this, container, savedInstanceState).apply {
            setPopsLastView(true)
            if(!hasRootController()) {
                setRoot(RouterTransaction.with(GenresController()))
            }
        }
    }

    override fun onBackPressed() {
        if (mainRouter?.handleBack() == false) {
            super.onBackPressed()
        }
    }
}
