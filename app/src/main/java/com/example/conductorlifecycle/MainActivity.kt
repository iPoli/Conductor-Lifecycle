package com.example.conductorlifecycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import kotlinx.android.synthetic.main.controller_home.view.*
import kotlinx.android.synthetic.main.controller_second.view.*

const val TAG = "ConductorLC"

class HomeController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_home, container, false)
        view.showNextButton.setOnClickListener {
            router.pushController(
                RouterTransaction.with(SecondController()).pushChangeHandler(
                    FadeChangeHandler()
                ).popChangeHandler(FadeChangeHandler())
            )
        }
        return view
    }

    override fun onAttach(view: View) {
        Log.d(TAG, "Home before attach")
        super.onAttach(view)
        Log.d(TAG, "Home after attach")
        val childRouter = getChildRouter(view.childRouterContainer, null)
        if (!childRouter.hasRootController()) {
            childRouter.setRoot(
                RouterTransaction.with(ChildController())
            )
        }
    }
}

class ChildController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_child, container, false)
    }

    override fun onAttach(view: View) {
        Log.d(TAG, "Child before attach")
        super.onAttach(view)
        Log.d(TAG, "Child after attach")
    }
}

class SecondController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_second, container, false)
        view.goBackButton.setOnClickListener {
            Log.d(TAG, "Pressing Back")
            router.handleBack()
        }
        return view
    }

}

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router =
            Conductor.attachRouter(this, findViewById(R.id.controllerContainer), savedInstanceState)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(HomeController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
