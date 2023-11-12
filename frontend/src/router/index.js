import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CreateNewCard from '../views/CreateNewCard.vue'
import AllCardView from '../views/AllCardView.vue'
import LearnView from '../views/LearnView.vue'
import AllDecksView from '../views/AllDecksView.vue'
import CreateNewDeck from '../views/CreateNewDeck.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/card/new',
      name: 'card_new',
      component: CreateNewCard
    },
    {
      path: '/cards',
      name: 'cards',
      component: AllCardView
    },
    {
      path: '/learn',
      name: 'learn',
      component: LearnView
    },
    {
      path: '/decks',
      name: 'decks',
      component: AllDecksView
    },
    {
      path: '/deck/new',
      name: 'deck_new',
      component: CreateNewDeck
    },
  ]
})

export default router
