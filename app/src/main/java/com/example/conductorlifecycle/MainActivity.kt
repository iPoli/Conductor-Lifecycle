package com.example.conductorlifecycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import kotlinx.android.synthetic.main.controller_home.view.*
import kotlinx.android.synthetic.main.controller_second.view.*
import timber.log.Timber

class HomeController : RestoreViewOnCreateController() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
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
        Timber.d("Home before attach")
        super.onAttach(view)
        Timber.d("Home after attach")
        val childRouter = getChildRouter(view.childRouterContainer, null)
        if (!childRouter.hasRootController()) {
            childRouter.setRoot(
                RouterTransaction.with(ChildController()).pushChangeHandler(
                    FadeChangeHandler()
                ).popChangeHandler(FadeChangeHandler())
            )
        }
    }
}

class ChildController : RestoreViewOnCreateController() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate(R.layout.controller_child, container, false)
    }

    override fun onAttach(view: View) {
        Timber.d("Child before attach")
        super.onAttach(view)
        Timber.d("Child after attach")
    }
}

class SecondController : RestoreViewOnCreateController() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.controller_second, container, false)
        view.goBackButton.setOnClickListener {
            Timber.d("Pressing Back")
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

        Timber.plant(Timber.DebugTree())

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
