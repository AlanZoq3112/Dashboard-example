<template>
  <div>
    <!-- Botón para abrir el modal -->
    <button @click="showModal = true">Mostrar Logs</button>

    <!-- Modal -->
    <div v-if="showModal">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tabla</th>
            <th>Operación</th>
            <th>Descripción</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in logs" :key="log.id">
            <td>{{ log.id }}</td>
            <td>{{ log.tabla }}</td>
            <td>{{ log.operacion }}</td>
            <td>{{ log.descripcion }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
  
  <script>
import MovieService from "../services/MovieService";

export default {
  data() {
    return {
      logs: [],
      showModal: false,
    };
  },
  created() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        this.logs = await MovieService.fetchLogs();
      } catch (error) {
        console.error("Error al obtener los logs:", error);
      }
    },
  },
};
</script>