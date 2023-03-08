<template>
  <div>
    <div class="content" @click="$emit('click')">
      <label>{{this.title}}</label>
    </div>
  </div>
</template>

<script>
export default {
props: {
  title: {
    type: String
  }
}
}
</script>

<style lang="stylus" scoped>
.content
  height 3rem
  display: flex
  align-items: center
  border-bottom: 0.01rem solid lightgray
</style>
