<template>
    <div class="provinceCity">
        <el-select v-model="province" placeholder="使用地区（省）" @change="provinceChanged">
            <!-- <el-option
            :key="''"
            :label="'全部'"
            :value="''">
            </el-option> -->
            <el-option
            v-for="item in provinces"
            :key="item.code"
            :label="item.name"
            :value="item.code">
            </el-option>
        </el-select>
        <el-select v-model="city"
                 :loading="loadingCity"
                 @change="cityChanged"
                 placeholder="使用地区（市）">
            <!-- <el-option
            :key="''"
            :label="'全部'"
            :value="''">
            </el-option> -->
            <el-option
            v-for="item in cities"
            :key="item.code"
            :label="item.name"
            :value="item.coordinate">
            </el-option>
        </el-select>
    </div>
</template>

<script>
   import provinceCity from 'static/provinceCity.json'
  export default {
    name: 'provinceCity',
    props: {
      provinceCode: {
        type: String,
        default: ''
      },
      cityCode: {
        type: String,
        default: ''
      }
    },
    created() {
      this.provinces = provinceCity.provinces
    },
    computed: {
 
    },
    mounted() {
      this.province = this.provinceCode
      this.provinceChanged(this.provinceCode)
      this.city = this.cityCode
    },
    data() {
      return {
        loadingCity: false,
        province: '',
        city: '',
        provinces: [],
        cities: []
      }
    },
    methods: {
      provinceChanged(value) {
        if (value !== '') {
          this.loadingCity = true
          for (var item of this.provinces) {
            if (item.code === value) {
              this.cities = item.cities
              this.city = ''
              this.loadingCity = false
              break
            } else {
              continue
            }
          }
        } else {
          this.cities = []
          this.city = ''
        }
        // this.$emit('selectChange', this.city)
      },
      cityChanged(value) {
        this.$emit('selectChange', this.city)
      },
    }
  }
</script>

<style>
    .provinceCity .el-select:first-child{
        margin-right: 30px;
    }
</style>
