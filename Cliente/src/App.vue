<script setup>
</script>
<template>
  <div>
    <b-navbar class="navbarstyle">
      <b-navbar-toggle target="nav-text-collapse"></b-navbar-toggle>
      <b-navbar-brand class="mx-auto">
        <b-img
          src="https://www.cinedot.com.mx/IT-CDN/Themes/_default/Images/color_logo.png"
          alt="Logo"
          width="250"
          height="auto"
        ></b-img>
      </b-navbar-brand>
    </b-navbar>
    <b-container fluid>
      <router-view />
    </b-container>
    <br />
    <br />
    <b-footer
      class="bg-dark text-white text-center"
      style="position: fixed; left: 0; bottom: 0; width: 100%; font-size: 12px"
    >
      Todos los derechos reservados ©
    </b-footer>
  </div>
</template>

<style scoped>
.navbarstyle {
  background-color: #e83e8c;
  color: #ffff;
  font-size: 30px;
}
</style>
