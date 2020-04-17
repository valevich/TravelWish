/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.travelwishlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_detail.*
import android.view.Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME
import android.view.Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME
import android.content.Intent
import android.view.Window


class MainActivity : AppCompatActivity() {

  lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
  lateinit private var adapter: TravelListAdapter
  lateinit private var menu: Menu
  private var isListView: Boolean = false

  private val onItemClickListener = object : TravelListAdapter.OnItemClickListener {
    override fun onItemClick(view: View, position: Int) {
      val transitionIntent = DetailActivity.newIntent(this@MainActivity, position)

      val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
      val statusBar = findViewById<View>(android.R.id.statusBarBackground)

      val imagePair = Pair.create(placeImage as View, "tImage")
      val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

      val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
      val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
      val toolbarPair = Pair.create(toolbar as View, "tActionBar")

      val pairs = mutableListOf(imagePair, holderPair, statusPair, toolbarPair)
      if (navigationBar != null) {
        pairs += navPair
      }

      val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
        *pairs.toTypedArray())
      ActivityCompat.startActivity(this@MainActivity, transitionIntent, options.toBundle())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    isListView = true

    staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    list.layoutManager = staggeredLayoutManager

    adapter = TravelListAdapter(this)
    adapter.setOnItemClickListener(onItemClickListener)
    list.adapter = adapter

    setUpActionBar()
  }

  private fun setUpActionBar() {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(false)
    supportActionBar?.setDisplayShowTitleEnabled(true)
    supportActionBar?.elevation = 7.0f
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    this.menu = menu
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    if (id == R.id.action_toggle) {
      toggle()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun toggle() {
    if (isListView) {
      showGridView()
    } else {
      showListView()
    }
  }

  private fun showListView() {
    staggeredLayoutManager.spanCount = 1
    val item = menu.findItem(R.id.action_toggle)
    item.setIcon(R.drawable.ic_action_grid)
    item.title = getString(R.string.show_as_grid)
    isListView = true
  }

  private fun showGridView() {
    staggeredLayoutManager.spanCount = 2
    val item = menu.findItem(R.id.action_toggle)
    item.setIcon(R.drawable.ic_action_list)
    item.title = getString(R.string.show_as_list)
    isListView = false
  }
}
