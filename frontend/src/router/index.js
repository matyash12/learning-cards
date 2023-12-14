import { createRouter, createWebHistory } from 'vue-router'
import CreateNewCard from '../views/CreateNewCard.vue'
import LearnView from '../views/LearnView.vue'
import CreateNewDeck from '../views/CreateNewDeck.vue'
import TridaView from '../views/TridaView.vue'
import DeckView from '../views/DeckView.vue';
import LoginView from '../views/LoginView.vue';
import CreateNewTrida from '../views/CreateNewTrida.vue';
import NotFoundView from '../views/NotFound.vue';
import RegisterView from '../views/RegisterView.vue';
import EditCardView from '../views/EditCardView.vue';
import ExportDataView from '../views/ExportDataView.vue';
import TridaEditView from '../views/TridaEditView.vue';
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: TridaView
      //place where user can see all trida and theirs decks
    },
    {
      path: '/deck/:id',
      name: 'deckinfo',
      component: DeckView
      //main place for editing deck
    },
    {
      path: '/deck/:deckid/:cardid',
      name: 'card_new',
      component: CreateNewCard
    },
    {
      path: '/learn/:deckid',
      name: 'learn',
      component: LearnView
    },
    {
      path: '/:tridaid/new',
      name: 'deck_new',
      component: CreateNewDeck,
    },
    {
      path: '/new',
      name: 'trida_new',
      component: CreateNewTrida
    }
    ,
    {
      path: '/user/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/:pathMatch(.*)*',
      component: NotFoundView
    },
    {
      path: '/user/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/deck/:deckid/:cardid/edit',
      name: 'editcard',
      component: EditCardView
    },
    {
      path: '/export',
      name: 'exportdata',
      component: ExportDataView,
    },
    {
      path: '/:tridaid/edit',
      name: 'edittrida',
      component: TridaEditView
    },
  ]
})

export default router
