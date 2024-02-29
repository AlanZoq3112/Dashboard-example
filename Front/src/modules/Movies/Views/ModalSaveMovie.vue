<template>
    <div>
        <div>
            <b-modal hide-footer hide-header centered id="modal-save-movie">
                <header class="text-center border-bottom">
                    <p style="font-family: cabin">Registrar pelicula</p>
                </header>

                <main>
                    <form id="registrarPelicula">
                        <b-row>
                            <b-col>
                                <label for="pelicula">Nombre de la pelicula: *</label>
                                <b-form-input v-model="pelicula.name" type="text" class="form-control"
                                    placeholder="Pelicula..." required :state="validarName"
                                    aria-describedby="input-live-help input-live-feedback" />

                                <b-form-invalid-feedback :state="validarName">
                                    Formato invalido
                                </b-form-invalid-feedback>


                            </b-col>
                            <b-col>
                                <label for="pelicula">Genero de la pelicula: *</label>
                                <b-form-select v-model="pelicula.genero" :state="validarGenero"
                                    :options="options"></b-form-select>

                                <b-form-invalid-feedback :state="validarGenero">
                                    Selecciona un género válido
                                </b-form-invalid-feedback>
                            </b-col>
                        </b-row>

                        <b-row>
                            <b-col>
                                <label for="pelicula">Director de la pelicula: *</label>
                                <b-form-input v-model="pelicula.director" type="text" class="form-control"
                                    placeholder="Director..." required :state="validarDirector"
                                    aria-describedby="input-live-help input-live-feedback" />

                                <b-form-invalid-feedback :state="validarDirector">
                                    Formato invalido
                                </b-form-invalid-feedback>


                            </b-col>
                            <b-col>
                                <label for="pelicula">Fecha de estreno: *</label>
                                <b-form-input v-model="pelicula.publishDate" type="date" class="form-control"
                                    placeholder="2024..." required :state="validarFecha"
                                    aria-describedby="input-live-help input-live-feedback" />

                                <b-form-invalid-feedback :state="validarFecha">
                                    {{ validarFecha ? 'Fecha no válida' : 'La fecha debe ser anterior a la fecha actual después del 22 de marzo de 1895' }}
                                </b-form-invalid-feedback>

                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col>
                                <label for="pelicula">Descripción de la pelicula: *</label>
                                <b-form-textarea id="textarea" v-model="pelicula.description"
                                    placeholder="Describe la pelicula..." rows="3" max-rows="6"
                                    :state="validarDescription"></b-form-textarea>

                                <b-form-invalid-feedback :state="validarDescription">
                                    Formato invalido (Coloca el puntero en el icono de abajo para saber los lineamientos del
                                    formato)
                                </b-form-invalid-feedback>

                            </b-col>
                        </b-row>
                        <b-icon v-b-tooltip.hover="{ variant: 'info' }"
                            title="En los campos no se aceptan caracteres especiales, no se permiten espacios en blanco, no se permite el campo vacio y no pueden ser mayores a 100 caracteres"
                            icon="exclamation-circle-fill" variant="secondary"></b-icon>
                    </form>

                </main>

                <footer class="text-center mt-5">
                    <button class="btn m-1 cancel" @click="onClose" id="savemovie">
                        Cancelar
                    </button>
                    <button class="btn m-1 success" @click="save" id="saveteam" :disabled="!validarForm" type="submit">
                        Registrar
                    </button>
                </footer>
            </b-modal>
        </div>
    </div>
</template>

<script>
import Swal from 'sweetalert2';
import axios from 'axios'
export default {
    name: "modal-save-movie",

    data() {
        return {
            pelicula: {
                id: null,
                name: "",
                description: "",
                director: "",
                genero: null, 
                publishDate: null 
            },
            selected: null,
            options: [
                { value: null, text: "Selecciona una opción" },
                { value: "Terror", text: "Terror" },
                { value: "Aventura", text: "Aventura" },
                { value: "Acción", text: "Acción" },
                { value: "Catástrofe", text: "Catástrofe" },
                { value: "Ciencia Ficción.", text: "Ciencia Ficción." },
                { value: "Comedia", text: "Comedia" },
                { value: "Documentales", text: "Documentales" },
                { value: "Drama", text: "Drama" },
                { value: 'Infantil', text: 'Infantil' },
            ],

        };
    },
    methods: {
        onClose() {
            this.$bvModal.hide("modal-save-movie");
            this.pelicula.name = ""
            this.pelicula.description = ""
            this.pelicula.genero = null,
                this.pelicula.dateCreation = "",
                this.pelicula.director = "",
                this.pelicula.publishDate = null

        },
        async save() {
            Swal.fire({
                title: "¿Estás seguro de registrar la pelicula?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#008c6f',
                cancelButtonColor: '#e11c24',
                confirmButtonText: "Confirmar",
                cancelButtonText: 'Cancelar',
            }).then(async (result) => {
                if (result.isConfirmed) {
                    try {
                        console.log(this.pelicula);
                        await axios.post("http://localhost:8080/api-movieBack/", this.pelicula);
                        Swal.fire({
                            title: "¡Guardada!",
                            text: "La pelicula se registró correctamente",
                            icon: "success"
                        });
                        this.onClose();
                        this.$emit('movie-updated');
                    } catch (error) {
                        console.log("Error al guardar la pelicula", error);
                    }

                }
            });
        },


    },
    computed: {
        validarName() {
            const regex = /^(?!.*[\s]{2,})(?!^\s)(?!.*\s$)(?!.*(\S)\1{2,})[a-zA-Z0-9\s\-_.,]*$/;
            return this.pelicula.name.length > 0 && this.pelicula.name.length < 100 && regex.test(this.pelicula.name);
        },
        validarDirector() {
            const regex = /^(?!.*[\s]{2,})(?!^\s)(?!.*\s$)(?!.*(\S)\1{2,})[a-zA-Z0-9\s\-_.,]*$/;
            return this.pelicula.director.length > 0 && this.pelicula.director.length < 100 && regex.test(this.pelicula.director);
        },
        validarDescription() {
            const regex = /^(?!.*[\s]{2,})(?!^\s)(?!.*\s$)(?!.*(\S)\1{2,})[a-zA-Z0-9\s\-_.,]*$/;
            return this.pelicula.description.length > 0 && this.pelicula.description.length < 100 && regex.test(this.pelicula.description);
        },
        validarGenero() {
            return this.pelicula.genero !== null;
        },
        validarFecha() {
            const selectedDate = new Date(this.pelicula.publishDate);
            const currentDate = new Date();
            const minDate = new Date('1895-03-22');
            return (
                selectedDate <= currentDate && selectedDate >= minDate
            );
        },

        validarForm() {
            return this.validarName && this.validarDescription && this.validarGenero && this.validarFecha;
        },

    }

}
</script>

<style scoped>
.success {
    font-family: Cabin;
    background-color: #009475;
    color: white;
}

.cancel {
    font-family: Cabin;
    background-color: #ffce50;
    color: black;
}
</style>