<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <div class="wrapper">
      
      <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
          <a class="navbar-brand" href="#">Cards</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
              <li class="nav-item">
                <RouterLink class="nav-link active" to="/">Home</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/about">About</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/card/new">New card</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/cards">All cards</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/learn">Learn</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/decks">All decks</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link active"  to="/deck/new">New deck</RouterLink>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

